package game.server.rest

import game.server.db.DBConfig
import game.server.login.BCrypt
import game.server.model.User
import io.requery.kotlin.eq
import mu.KotlinLogging
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Todo: separate DB for passwords
 * username | password
 * send user details as body because URLs/parameters are usually output to logs
 */

private val logger = KotlinLogging.logger {}

@Path("/users/")
class Users {
    /**
     * On successful login give back session token
     */
    @POST()
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun login(user: UserLogin): Response {
        logger.info { "login called for ${user.userName}"}
        if(user.userName.isNullOrBlank() || user.password.isNullOrBlank())
            return Response.status(400).entity(Error("Username or password cannot be blank")).build()

        val result = DBConfig.data select (User::class) where (User::name eq user.userName) limit 1

        val hashedPW = result.get().firstOrNull()?.password

        if(hashedPW != null && BCrypt.checkpw(user.password, hashedPW)) {
            return Response.ok().entity(Token("asdf")).build();
        } else
            return Response.status(400).entity(Error("UserLogin/password not recognized")).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST()
    @Path("register")
    fun register(user: UserLogin): Response {
        logger.debug { "register called for ${user.userName}" }
        if(user.userName.isNullOrBlank() || user.password.isNullOrBlank())
            return Response.status(400).entity(Error("Username or password cannot be blank")).build()

        val hashedPW = BCrypt.hashpw(user.password, BCrypt.gensalt())

        DBConfig.data insert User(user.userName!!, hashedPW)

        return Response.ok(user).build();
    }

}

data class UserLogin(val userName: String?, val password: String?)

data class Token(val token: String)

data class Error(val message: String)
