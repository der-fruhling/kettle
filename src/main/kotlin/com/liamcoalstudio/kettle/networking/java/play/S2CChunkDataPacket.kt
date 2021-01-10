package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Block
import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.world.*
import net.querz.nbt.io.NBTSerializer
import net.querz.nbt.io.NamedTag
import net.querz.nbt.tag.CompoundTag

class S2CChunkDataPacket @ExperimentalStdlibApi constructor(
    val player: Player, val world: World, val x: Long, val z: Long
) : Packet(0x20, null) {
    @ExperimentalStdlibApi
    override fun write(buf: Buffer) {
        val cbuf = Buffer()

        buf.addInt(x.toInt())
        buf.addInt(z.toInt())
        buf.addBoolean(true)

        var bitmask = 0
        val chunkYList = mutableListOf<Long>()

        for(i in 0L..15L) {
            bitmask = bitmask or ((if(world[Position(x, i, z)].blocks.all { it.value == Block.air }) 0 else 1) shl i.toInt())
            chunkYList.add(i)
        }

        buf.addVarInt(bitmask)

        val compound = CompoundTag()
        val heightmap = CompoundTag()
        heightmap.putLongArray("MOTION_BLOCKING", LongArray(36) { Long.MAX_VALUE })
        compound.put("Heightmap", heightmap)
        buf.addBuffer(Buffer(NBTSerializer(false).toBytes(NamedTag("_", compound))))

        buf.addVarInt(1024)
        for(i in 0 until 1024)
            buf.addVarInt(Biome.OCEAN.id)

        for(y in chunkYList) {
            world[Position(x, y, z)].write(cbuf)
        }
        buf.addVarInt(cbuf.array.size)
        buf.addBuffer(cbuf)

        buf.addVarInt(0)
    }
}