package com.liamcoalstudio.kettle.helpers

import kotlinx.serialization.Serializable

@Serializable
data class ServerConfiguration(
    var motd: String = "A tea cup transmitting over the internet.",
    var viewDistance: Int = 8,
    var gamemode: Int = 0,
    var spawn: ConfiguredSpawnLocation = ConfiguredSpawnLocation(),
    var flat: Boolean = false
)
