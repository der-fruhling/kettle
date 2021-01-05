package com.liamcoalstudio.kettle.networking.java.status

import com.google.gson.Gson
import com.liamcoalstudio.kettle.helpers.serverlist.java.*
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.servers.java.JavaServer

class S2CStatusResponsePacket(val serverState: ServerState?) : Packet(0x00, null) {
    override fun write(buf: Buffer) {
        val json = ServerListJSON()
        json.description = Text()
        json.description.text = "Kettle Kotlin Test"
        json.players = ServerListPlayerInfo()
        json.players.max = 256
        json.players.online = 0
        json.players.sample = MutableList(0) { ServerListPlayerEntry() }
        json.version = ServerListVersion()
        json.version.name = "KT1.16.4"
        json.version.protocol = JavaServer.PROTOCOL_NUMBER
        buf.addString(Gson().toJson(json))
    }
}