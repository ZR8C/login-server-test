package game.server.rest

import javax.ws.rs.ApplicationPath
import javax.ws.rs.core.Application

@ApplicationPath("/")
class ServerApplication: Application() {

    override fun getClasses(): MutableSet<Class<*>> {
        return mutableSetOf(Users::class.java)
    }
}