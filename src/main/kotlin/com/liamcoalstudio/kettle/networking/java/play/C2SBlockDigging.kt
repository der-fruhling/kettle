package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Block
import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.helpers.Dimension
import com.liamcoalstudio.kettle.logging.ConsoleLogger
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.ClientState
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.networking.main.packets.Producer
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.servers.main.KettleServer
import com.liamcoalstudio.kettle.world.Position

class C2SBlockDigging : Packet(0x1b, ClientState.Status.Play), Producer<Packet> {
    var status: Int = -1
    var location: Position? = null
    var face: Byte = 0

    @ExperimentalUnsignedTypes
    override fun read(buf: Buffer) {
        status = buf.getVarInt()
        location = Position.read(buf)
        face = buf.getByte()
        location!!.y -= 1
    }

    @ExperimentalStdlibApi
    override fun updateOnRead(state: ServerState, client: Client) {
        val world = KettleServer.GLOBAL!!.get().worlds[Dimension.OVERWORLD]!!
        val player = world.players.find { it.client == client }!!
        when(status) {
            0, 2 -> world.setBlockAt(location!!, Block.air)
        }
        ConsoleLogger(C2SBlockDigging::class).info("Modify $location = ${KettleServer.GLOBAL!!.get().worlds[Dimension.OVERWORLD]!!.getBlockAt(location!!)}")

    }

    override fun produce(serverState: ServerState) = C2SBlockDigging()
}