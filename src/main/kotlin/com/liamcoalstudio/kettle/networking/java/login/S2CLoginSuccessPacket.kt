package com.liamcoalstudio.kettle.networking.java.login

import com.liamcoalstudio.kettle.nbt.NBTTag
import com.liamcoalstudio.kettle.nbt.comp.NBTCompound
import com.liamcoalstudio.kettle.nbt.comp.NBTList
import com.liamcoalstudio.kettle.nbt.comp.NBTRootCompound
import com.liamcoalstudio.kettle.nbt.comp.NBTString
import com.liamcoalstudio.kettle.nbt.prim.NBTByte
import com.liamcoalstudio.kettle.nbt.prim.NBTFloat
import com.liamcoalstudio.kettle.nbt.prim.NBTInt
import com.liamcoalstudio.kettle.networking.java.play.S2CJoinGamePacket
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.ClientState
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.servers.java.JavaServer
import java.util.*
import com.liamcoalstudio.kettle.nbt.prim.NBTByte.Companion.nfalse
import com.liamcoalstudio.kettle.nbt.prim.NBTByte.Companion.ntrue
import com.liamcoalstudio.kettle.nbt.prim.NBTDouble

class S2CLoginSuccessPacket(private val username: String) : Packet(0x02, null) {
    override fun write(buf: Buffer) {
        buf.addUUID(UUID.nameUUIDFromBytes(username.toByteArray()))
        buf.addString(username)
    }

    override fun updateOnWrite(state: ServerState, client: Client) {
        JavaServer.GLOBAL_CONTROLLER!!.get().execute {
            client.status = ClientState.Status.Play
            val codec = createDimensionCodec()
            client.send(S2CJoinGamePacket(
                eid = 0,
                hardcore = false,
                gamemode = 0,
                prevGamemode = -1,
                worldCount = 1,
                worlds = arrayOf("minecraft:overworld"),
                dimensionCodec = codec["_"],
                dimension = codec["_"]["minecraft:dimension_type"]["value"][0]["element"],
                hashedSeed = 0,
                maxPlayers = 256,
                enableRespawnScreen = true,
                isDebug = false,
                isFlat = false,
                reducedDebugInfo = false,
                viewDistance = 32,
                worldName = "minecraft:overworld"
            ))
        }
    }

    private fun createDimensionCodec(): NBTTag {
        val compound = NBTRootCompound()
        compound["_"] = NBTCompound()
        compound["_"]["minecraft:dimension_type"] = NBTCompound()
        compound["_"]["minecraft:dimension_type"]["type"] = NBTString("minecraft:dimension_type")
        compound["_"]["minecraft:dimension_type"]["value"] = NBTList(1)
        compound["_"]["minecraft:dimension_type"]["value"][0] =
            createCodecOverworld(compound["_"]["minecraft:dimension_type"]["value"][0])
        compound["_"]["minecraft:worldgen/biome"] = NBTCompound()
        compound["_"]["minecraft:worldgen/biome"]["type"] = NBTString("minecraft:worldgen/biome")
        compound["_"]["minecraft:worldgen/biome"]["value"] = NBTList(1)
        compound["_"]["minecraft:worldgen/biome"]["value"][0] =
            createPlainsBiome(compound["_"]["minecraft:worldgen/biome"]["value"][0])
        return compound
    }

    private fun createCodecOverworld(parent: NBTTag): NBTTag {
        val compound = NBTCompound(parent)
        compound["name"] = NBTString("minecraft:overworld")
        compound["id"] = NBTInt(0)
        compound["element"] = NBTCompound()
        compound["element"]["piglin_safe"] = NBTByte(nfalse)
        compound["element"]["natural"] = NBTByte(ntrue)
        compound["element"]["ambient_light"] = NBTFloat(0.0f)
        compound["element"]["infiniburn"] = NBTString("minecraft:infiniburn_overworld")
        compound["element"]["respawn_anchor_works"] = NBTByte(nfalse)
        compound["element"]["has_skylight"] = NBTByte(ntrue)
        compound["element"]["bed_works"] = NBTByte(ntrue)
        compound["element"]["effects"] = NBTString("minecraft:overworld")
        compound["element"]["has_raids"] = NBTByte(ntrue)
        compound["element"]["logical_height"] = NBTInt(256)
        compound["element"]["coordinate_scale"] = NBTDouble(1.0)
        compound["element"]["ultrawarm"] = NBTByte(nfalse)
        compound["element"]["has_ceiling"] = NBTByte(nfalse)
        return compound
    }

    private fun createPlainsBiome(parent: NBTTag): NBTTag {
        val compound = NBTCompound(parent)
        compound["name"] = NBTString("minecraft:plains")
        compound["id"] = NBTInt(0)
        compound["element"] = NBTCompound()
        compound["element"]["precipitation"] = NBTString("rain")
        compound["element"]["depth"] = NBTFloat(0.125f)
        compound["element"]["temperature"] = NBTFloat(0.8f)
        compound["element"]["scale"] = NBTFloat(0.05f)
        compound["element"]["downfall"] = NBTFloat(0.4f)
        compound["element"]["category"] = NBTString("plains")
        compound["element"]["effects"] = NBTCompound()
        compound["element"]["effects"]["sky_color"] = NBTInt(7907327)
        compound["element"]["effects"]["water_fog_color"] = NBTInt(329011)
        compound["element"]["effects"]["fog_color"] = NBTInt(12638463)
        compound["element"]["effects"]["water_color"] = NBTInt(4159204)
        compound["element"]["effects"]["mood_sound"] = NBTCompound()
        compound["element"]["effects"]["mood_sound"]["tick_delay"] = NBTInt(6000)
        compound["element"]["effects"]["mood_sound"]["offset"] = NBTDouble(2.0)
        compound["element"]["effects"]["mood_sound"]["sound"] = NBTString("minecraft:ambient.cave")
        compound["element"]["effects"]["mood_sound"]["block_search_extent"] = NBTInt(8)
        return compound
    }
}