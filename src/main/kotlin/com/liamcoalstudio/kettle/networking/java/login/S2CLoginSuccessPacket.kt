package com.liamcoalstudio.kettle.networking.java.login

import com.liamcoalstudio.kettle.networking.java.play.S2CJoinGamePacket
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.ClientState
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.helpers.Dimension
import com.liamcoalstudio.kettle.helpers.KettleProperties
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.servers.java.JavaServer
import com.liamcoalstudio.kettle.world.Biome
import net.querz.nbt.io.NBTUtil
import net.querz.nbt.io.SNBTUtil
import net.querz.nbt.tag.CompoundTag
import net.querz.nbt.tag.ListTag
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class S2CLoginSuccessPacket(private val username: String) : Packet(0x02, null) {
    override fun write(buf: Buffer) {
        buf.addUUID(UUID.nameUUIDFromBytes(username.toByteArray()))
        buf.addString(username)
    }

    override fun updateOnWrite(state: ServerState, client: Client) {
        JavaServer.GLOBAL_CONTROLLER!!.get().execute {
            client.status = ClientState.Status.Play
//            val codec = createDimensionCodec()
            val codec = SNBTUtil.fromSNBT(Files.readString(Path.of("objects/dimcodec.test.snbt"))) as CompoundTag
            NBTUtil.write(codec, File("test.nbt"))
            client.send(S2CJoinGamePacket(
                eid = 0,
                hardcore = false,
                gamemode = KettleProperties.gamemode.toByte(),
                prevGamemode = -1,
                worldCount = 1,
                worlds = arrayOf("minecraft:overworld"),
                dimensionCodec = codec,
                dimension = (codec.getCompoundTag("minecraft:dimension_type").getListTag("value") as ListTag<CompoundTag>)[0]["element"],
                hashedSeed = 0,
                maxPlayers = 256,
                enableRespawnScreen = true,
                isDebug = false,
                isFlat = false,
                reducedDebugInfo = false,
                viewDistance = KettleProperties.viewDistance,
                worldName = "minecraft:overworld"
            ))
        }
    }

    private fun createDimensionCodec(): CompoundTag {
        val compound = CompoundTag()

        val dimType = CompoundTag()
        dimType.putString("type", "minecraft:dimension_type")
        val dimTypeValue = ListTag(CompoundTag::class.java)
        Dimension.values().forEach { dim ->
            dimTypeValue.add(dim.codec)
        }
        dimType.put("value", dimTypeValue)
        compound.put("minecraft:dimension_type", dimType)

        val biomeType = CompoundTag()
        biomeType.putString("type", "minecraft:worldgen/biome")
        val biomeTypeValue = ListTag(CompoundTag::class.java)
        Biome.values().forEach { dim ->
            biomeTypeValue.add(dim.codec)
        }
        biomeType.put("value", biomeTypeValue)
        compound.put("minecraft:worldgen/biome", biomeType)

        return compound
    }
}