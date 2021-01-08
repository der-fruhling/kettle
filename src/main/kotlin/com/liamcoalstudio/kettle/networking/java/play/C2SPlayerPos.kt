package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.helpers.Dimension
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.ClientState
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.networking.main.packets.Producer
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.servers.java.JavaServer
import com.liamcoalstudio.kettle.servers.main.KettleServer
import kotlin.math.floor
import kotlin.math.hypot

class C2SPlayerPos : Packet(0x12, ClientState.Status.Play), Producer<Packet> {
    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0

    override fun read(buf: Buffer) {
        x = buf.getDouble()
        y = buf.getDouble()
        z = buf.getDouble()
        buf.getBoolean()
    }

    @ExperimentalStdlibApi
    override fun updateOnRead(state: ServerState, client: Client) {
        val player = KettleServer.GLOBAL!!.get().worlds[Dimension.OVERWORLD]!!.players.find { it.client == client }!!
        var update = false
        if (floor(player.y).toInt() != floor(y).toInt() ||
            floor(player.x / 16).toInt() != floor(x / 16).toInt() ||
            floor(player.z / 16).toInt() != floor(z / 16).toInt()
        ) update = true
        player.x = x
        player.y = y
        player.z = z
        if(update) player.updateChunks().start()
        JavaServer.GLOBAL_CONTROLLER!!.get().execute {
            player.client!!.send(S2CUpdateViewPosition(floor(x / 16.0).toInt(), floor(z / 16.0).toInt()))
        }
    }

    override fun produce(serverState: ServerState) = C2SPlayerPos()
}