package game.server

import game.server.rest.ServerApplication
import org.glassfish.jersey.jetty.JettyHttpContainerFactory
import org.glassfish.jersey.server.ResourceConfig
import javax.ws.rs.core.UriBuilder

fun main(args: Array<String>) {
    println("test")

    val uri = UriBuilder.fromUri("http://localhost/").port(9080).build()

    val resource = ResourceConfig.forApplicationClass(ServerApplication::class.java)

    val jettyServer = JettyHttpContainerFactory.createServer(uri, resource)

    try {
        jettyServer.start();
        jettyServer.join();
    } finally {
        jettyServer.destroy();
    }


}

