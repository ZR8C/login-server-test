package game.server.login

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import game.server.db.DBConfig
import game.server.model.Session
import game.server.model.SessionEntity
import game.server.model.User
import io.requery.kotlin.*
import mu.KotlinLogging
import java.security.SecureRandom
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Holds session tokens. Sessions are held for an hour after the last access providing they are on this node.
 * sessions from another node are persisted in the database for an hour since first login
 */

private val logger = KotlinLogging.logger {}

class TokenCache {
    companion object {
        val cache: Cache<String, String> = Caffeine.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build();

        fun createSession(user: User) : String {
            val token = generateToken()
            cache.put(token, user.name)

            //add session to DB
            val session = SessionEntity()
            session.token = token
            session.user = user
            session.created = ZonedDateTime.now(ZoneId.of("UTC")) //todo can requery generate this?
            DBConfig.data insert session

            logger.debug { "Session created for user ${user} with token $token" }

            return token
        }

        private fun generateToken() : String {
            val secureRandom = SecureRandom()
            val byteArray = ByteArray(16)
            secureRandom.nextBytes(byteArray)
            return Base64.getEncoder().encodeToString(byteArray)!!
        }

        fun retrieveTokenFromDB(token: String?): String? {
            val anHourAgo = ZonedDateTime.now(ZoneId.of("UTC")).minusHours(1)
            val result = DBConfig.data.select(Session::class) where(Session::token eq token) and (Session::created gte anHourAgo) orderBy(Session::created.desc())

            val session = result.get().firstOrNull() ?: return null

            logger.debug { "Token retrieved from DB $token = ${session.user.name}"}

            return session.user.name
        }

        fun validateToken(token: String): String? {
            return cache.get(token, this::retrieveTokenFromDB)
        }
    }
}