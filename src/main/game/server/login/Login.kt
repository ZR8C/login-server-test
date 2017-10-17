package game.server.login

import game.server.db.DBConfig
import game.server.model.User
import game.server.model.UserEntity
import io.requery.kotlin.eq
import io.requery.kotlin.select
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class LoginException(override val message: String) : Exception(message)

fun login(userName: String, password: String): String {
    val result = DBConfig.data select(User::class) where (User::name eq userName) limit 1

    val user = result.get().firstOrNull()
    val hashedPW = user?.password ?: throw LoginException("Entry not found")

    if(!BCrypt.checkpw(password, hashedPW))
        throw LoginException("Password invalid")

    return TokenCache.createSession(user)
}

fun register(userName: String, password: String): Boolean {
    val hashedPW = BCrypt.hashpw(password, BCrypt.gensalt())

    try {
        val user = UserEntity()
        user.name = userName
        user.password = hashedPW
        DBConfig.data insert user
    } catch (e: RuntimeException) {
        logger.error("Register account failed for user $userName", e)
        return false
    }

    return true
}

fun validateToken(token: String): String? {
    return TokenCache.validateToken(token)
}