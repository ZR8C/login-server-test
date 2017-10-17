package game.server.model

import io.requery.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

@Entity
interface Session : Persistable {
    @get:Key
    var token: String

    var created: ZonedDateTime?

    @get:ForeignKey()
    @get:ManyToOne(cascade = arrayOf(CascadeAction.NONE))
    var user: User
}