package game.server.rest

import game.server.crypto.BCrypt
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Todo: separate DB for passwords
 * username | password
 * send user details as body because URLs/parameters are usually output to logs
 */

@Path("/users/")
class Users {
    companion object {
        val userPassword = mutableMapOf<String, String>();
    }

    /**
     * On successful login give back session token
     */
    @POST()
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun login(user: User): Response? {
        if(user.userName.isNullOrBlank() || user.password.isNullOrBlank())
            return Response.status(400).entity(Error("Username or password cannot be blank")).build()

        val hashedPW = userPassword[user.userName]

        if(hashedPW != null && BCrypt.checkpw(user.password, hashedPW)) {
            return Response.ok().entity(Token("asdf")).build();
        } else
            return Response.status(400).entity(Error("User/password not recognized")).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST()
    @Path("register")
    fun register(user: User): Response? {

        if(user.userName.isNullOrBlank() || user.password.isNullOrBlank())
            return Response.status(400).entity(Error("Username or password cannot be blank")).build()

        userPassword.put(user.userName!!, BCrypt.hashpw(user.password, BCrypt.gensalt()))

        return Response.ok(user).build();
    }

}

data class User(val userName: String?, val password: String?)

data class Token(val token: String)

data class Error(val message: String)