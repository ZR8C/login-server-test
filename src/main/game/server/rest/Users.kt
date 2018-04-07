package game.server.rest

import game.server.db.DBConfig
import game.server.login.*
import game.server.model.User
import io.requery.kotlin.eq
import io.requery.kotlin.select
import mu.KotlinLogging
import java.net.URI
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
    fun loginUser(user: UserLogin): Response {
        logger.info { "login called for ${user.userName}"}
        if(user.userName.isNullOrBlank() || user.password.isNullOrBlank())
            return Response.status(400).entity(Error("Username or password cannot be blank")).build()

        try {
            val token = login(user.userName!!, user.password!!)
            return Response.ok().entity(Token(token)).build();
        } catch (e: LoginException) {
            logger.error("Login failed for user ${user.userName}")
            return Response.status(400).entity(Error("Username/password not recognized")).build();
        }
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST()
    @Path("register")
    fun registerUser(user: UserLogin): Response {
        logger.debug { "register called for ${user.userName}" }
        if(user.userName.isNullOrBlank() || user.password.isNullOrBlank())
            return Response.status(400).entity(Error("Username or password cannot be blank")).build()

        if(register(user.userName!!, user.password!!))
            return Response.created(URI.create("users/${user.userName}")).build();
        else
            return Response.status(400).entity(Error("Failed to register account: ${user.userName}")).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST()
    @Path("checktoken")
    fun checkToken(token: Token): Response {
        logger.debug { "do called for $token" }

        val userName = validateToken(token.token)

        if(userName == null) return Response.status(400).entity(Error("Invalid token $token")).build()

        return Response.ok(UserLogin(userName,null)).build()
    }
}

data class UserLogin(val userName: String?, val password: String?)

data class Token(val token: String)

data class Error(val message: String)
