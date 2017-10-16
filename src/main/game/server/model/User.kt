package game.server.model

import io.requery.Entity
import io.requery.Key

@Entity
data class User constructor(
        @get:Key
        var name: String,
        var password: String
)