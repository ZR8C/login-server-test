package game.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import game.server.rest.ServerApplication
import org.glassfish.jersey.jetty.JettyHttpContainerFactory
import org.glassfish.jersey.server.ResourceConfig
import org.h2.jdbcx.JdbcDataSource
import javax.sql.DataSource
import javax.ws.rs.core.UriBuilder
import javax.ws.rs.ext.ContextResolver

fun main(args: Array<String>) {
    println("test")

    val uri = UriBuilder.fromUri("http://localhost/").port(9080).build()

    val resource = ResourceConfig.forApplication(ServerApplication())
            .register(ContextResolver<ObjectMapper> { ObjectMapper().registerModule(KotlinModule()) })

    val jettyServer = JettyHttpContainerFactory.createServer(uri, resource)

    try {
        jettyServer.start();
        jettyServer.join();
    } finally {
        jettyServer.destroy();
    }
}

