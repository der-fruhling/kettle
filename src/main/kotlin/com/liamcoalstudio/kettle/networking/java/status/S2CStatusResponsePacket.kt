package com.liamcoalstudio.kettle.networking.java.status

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.helpers.KettleProperties
import com.liamcoalstudio.kettle.helpers.serverlist.*
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.servers.java.JavaServer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class S2CStatusResponsePacket(val serverState: ServerState?) : Packet(0x00, null) {
    override fun write(buf: Buffer) {
        val json = ServerListJSON(
            description = Text(KettleProperties.motd),
            players = ServerListPlayerInfo(
                max = 256,
                online = 0,
                sample = MutableList(0) { ServerListPlayerEntry("null", "null") }
            ),
            version = ServerListVersion("1.16.4", JavaServer.PROTOCOL_NUMBER)
        )
        buf.addString(Json.encodeToString(json))
    }
}
