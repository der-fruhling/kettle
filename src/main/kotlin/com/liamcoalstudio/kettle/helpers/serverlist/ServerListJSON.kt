package com.liamcoalstudio.kettle.helpers.serverlist

import kotlinx.serialization.Serializable

@Serializable
data class ServerListJSON(
    var version: ServerListVersion,
    var players: ServerListPlayerInfo,
    var description: Text
)
