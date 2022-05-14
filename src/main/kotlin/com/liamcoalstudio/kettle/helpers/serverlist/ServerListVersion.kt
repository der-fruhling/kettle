package com.liamcoalstudio.kettle.helpers.serverlist

import kotlinx.serialization.Serializable

@Serializable
data class ServerListVersion(
    var name: String,
    var protocol: Int
)
