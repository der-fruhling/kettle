package com.liamcoalstudio.kettle.world

import net.querz.nbt.tag.CompoundTag
import net.querz.nbt.tag.IntArrayTag
import net.querz.nbt.tag.ListTag
import java.util.*

object WorldNBTEncoder {
    fun encodeChunk(x: Long, y: Long, z: Long, index: Int): CompoundTag {
        val tag = CompoundTag()
        tag.putLong("X", x)
        tag.putLong("Y", y)
        tag.putLong("Z", z)
        tag.putInt("Index", index)
        return tag
    }

    @ExperimentalStdlibApi
    fun encode(world: World, chunkList: ListTag<IntArrayTag>): CompoundTag {
        val tag = CompoundTag()
        tag.putString("Name", world.dimension.name.lowercase(Locale.getDefault()))
        tag.putString("Type", world.dimension.name)

        val size = CompoundTag()
        size.putLong("MaxChunkX", 32L)
        size.putLong("MaxChunkZ", 32L)
        size.putLong("MinChunkX", -32L)
        size.putLong("MinChunkZ", -32L)
        tag.put("Size", size)

        val chunks = ListTag(CompoundTag::class.java)
        world.chunks.forEach { (t, u) ->
            chunks.add(encodeChunk(t.x, t.y, t.z, chunkList.size()))
            chunkList.add(chunkList.size(), IntArrayTag(u.blocks.values.toIntArray()))
        }

        tag.put("Chunks", chunks)

        return tag
    }
}