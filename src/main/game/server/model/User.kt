package game.server.model

import io.requery.*
import java.time.ZonedDateTime

@Entity
interface User : Persistable {
        @get:Key
        var name: String
        var password: String

        @get:OneToMany(cascade = arrayOf(CascadeAction.NONE))
        val sessions: List<Session>
}