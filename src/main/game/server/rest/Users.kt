package game.server.rest

import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("/users/")
class Users {

    @GET()
    fun login() = "Logged in"
}