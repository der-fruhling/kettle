package com.liamcoalstudio.kettle.networking.java.play

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
import kotlin.properties.Delegates

class C2SBlockPlacement : Packet(0x2e, ClientState.Status.Play), Producer<Packet> {
    var hand: Int by Delegates.notNull()
    lateinit var location: Position
    var face: Int by Delegates.notNull()
    var cursorX by Delegates.notNull<Float>()
    var cursorY by Delegates.notNull<Float>()
    var cursorZ by Delegates.notNull<Float>()
    var insideBlock by Delegates.notNull<Boolean>()

    @ExperimentalUnsignedTypes
    override fun read(buf: Buffer) {
        hand = buf.getVarInt()
        location = Position.read(buf)
        face = buf.getVarInt()
        cursorX = buf.getFloat()
        cursorY = buf.getFloat()
        cursorZ = buf.getFloat()
        insideBlock = buf.getBoolean()
    }

    @ExperimentalStdlibApi
    override fun updateOnRead(state: ServerState, client: Client) {
        KettleServer.GLOBAL!!.get().execute {
            val player = KettleServer.player(client)
            if (player.inventory[player.selected + 36].block != null) {
                KettleServer.GLOBAL!!.get().worlds[Dimension.OVERWORLD]!![location] =
                    player.inventory[player.selected + 36].block!!
                ConsoleLogger(C2SBlockPlacement::class).info("Modify $location = ${KettleServer.GLOBAL!!.get().worlds[Dimension.OVERWORLD]!![location][location.toChunkPos()]}")
            }
        }
    }

    override fun produce(serverState: ServerState) = C2SBlockPlacement()
}