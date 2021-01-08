package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.ClientState
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.helpers.Dimension
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.servers.java.JavaServer
import com.liamcoalstudio.kettle.servers.main.KettleServer
import net.querz.nbt.io.NBTSerializer
import net.querz.nbt.io.NamedTag
import net.querz.nbt.tag.Tag

class S2CJoinGamePacket(
    val eid: Int,
    val hardcore: Boolean,
    val gamemode: Byte,
    val prevGamemode: Byte,
    val worldCount: Int,
    val worlds: Array<String>,
    val dimensionCodec: Tag<*>,
    val dimension: Tag<*>,
    val worldName: String,
    val hashedSeed: Long,
    val maxPlayers: Int,
    val viewDistance: Int,
    val reducedDebugInfo: Boolean,
    val enableRespawnScreen: Boolean,
    val isDebug: Boolean,
    val isFlat: Boolean
) : Packet(0x24, ClientState.Status.Play) {
    override fun write(buf: Buffer) {
        buf.addInt(eid)
        buf.addBoolean(hardcore)
        buf.addByte(gamemode)
        buf.addByte(prevGamemode)
        buf.addVarInt(worldCount)
        worlds.forEach { buf.addString(it) }
        buf.addBuffer(Buffer(NBTSerializer(false).toBytes(NamedTag("dimension_codec", dimensionCodec))))
        buf.addBuffer(Buffer(NBTSerializer(false).toBytes(NamedTag("dimension", dimension))))
        buf.addString(worldName)
        buf.addLong(hashedSeed)
        buf.addVarInt(maxPlayers)
        buf.addVarInt(viewDistance)
        buf.addBoolean(reducedDebugInfo)
        buf.addBoolean(enableRespawnScreen)
        buf.addBoolean(isDebug)
        buf.addBoolean(isFlat)
    }

    @ExperimentalStdlibApi
    override fun updateOnWrite(state: ServerState, client: Client) {
        KettleServer.GLOBAL!!.get().execute {
            KettleServer.GLOBAL!!.get().onPlayerJoin(client)
        }
    }
}