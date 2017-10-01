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
    @Produces(MediaType.TEXT_PLAIN)
    fun login(customer: Customer): Response? {
        if(customer.userName.isBlank() || customer.password.isBlank())
            return Response.status(400).entity("Username or password cannot be blank").build()

        val hashedPW = userPassword.get(customer.userName)

        if(BCrypt.checkpw(customer.password, hashedPW)) {
            return Response.ok().entity("Login successful").build();
        } else
            return Response.status(400).entity("User/password not recognized").build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST()
    @Path("register")
    fun register(customer: Customer): Response? {

        if(customer.userName.isBlank() || customer.password.isBlank())
            return Response.status(400).entity("Username or password cannot be blank").build()

        userPassword.put(customer.userName, BCrypt.hashpw(customer.password, BCrypt.gensalt()))

        return Response.ok(customer).build();
    }

}

data class Customer(val userName: String, val password: String)