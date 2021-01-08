package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.helpers.Dimension
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.ClientState
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.networking.main.packets.Producer
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.servers.main.KettleServer

class C2SClientSettings : Packet(0x05, ClientState.Status.Play), Producer<Packet> {
    var locale: String = ""
    var viewDistance: Byte = 2
    var chatMode = 2
    var chatColors = true
    var skinParts: Byte = 0b01111111
    var mainHand = 1

    override fun read(buf: Buffer) {
        locale = buf.getString()
        viewDistance = buf.getByte()
        chatMode = buf.getVarInt()
        chatColors = buf.getBoolean()
        skinParts = buf.getByte()
        mainHand = buf.getVarInt()
    }

    override fun updateOnRead(state: ServerState, client: Client) {
        val player = KettleServer.GLOBAL!!.get().worlds[Dimension.OVERWORLD]!!.players.find { it.client == client }!!
        player.locale = locale
        player.renderDistance = viewDistance.toInt()
        player.chatMode = chatMode.toByte()
        player.chatColors = chatColors
        player.displayedSkinParts = skinParts.toInt()
        player.mainHand = mainHand
    }

    override fun produce(serverState: ServerState) = C2SClientSettings()
}