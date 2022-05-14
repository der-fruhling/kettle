package com.liamcoalstudio.kettle.helpers.serverlist

import kotlinx.serialization.Serializable

@Serializable
data class ServerListPlayerInfo(
    var max: Int = 0,
    var online: Int = 0,
    var sample: List<ServerListPlayerEntry>
)
