package com.liamcoalstudio.kettle.helpers.serverlist

import kotlinx.serialization.Serializable

@Serializable
data class ServerListPlayerEntry(
    var name: String,
    var id: String
)
