package com.liamcoalstudio.kettle.servers.main

import com.google.gson.Gson
import com.liamcoalstudio.kettle.helpers.Dimension
import com.liamcoalstudio.kettle.helpers.KettleProperties
import com.liamcoalstudio.kettle.helpers.LambdaTimerTask
import com.liamcoalstudio.kettle.helpers.RecipeUnlockAction
import com.liamcoalstudio.kettle.logging.ConsoleLogger
import com.liamcoalstudio.kettle.networking.java.play.*
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.servers.java.JavaServer
import com.liamcoalstudio.kettle.world.*
import net.querz.nbt.io.NBTUtil
import net.querz.nbt.io.NamedTag
import net.querz.nbt.tag.CompoundTag
import net.querz.nbt.tag.IntArrayTag
import net.querz.nbt.tag.ListTag
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.max
import kotlin.random.Random

class KettleServer {
    var isFullyInitialized: Boolean = false
    private val executor = QueueExecutor()
    val logger = ConsoleLogger(KettleServer::class)
    val timer = Timer()
    val worlds = EnumMap<Dimension, World>(Dimension::class.java)

    fun execute(runnable: Runnable) {
        executor.execute(runnable)
    }

    private fun tick() {
        executor.run()
    }

    private fun shouldTick(): Boolean = true

    @ExperimentalStdlibApi
    private fun initWorlds() {
        val world = if(KettleProperties.flat) World.flat() else World.noise(Random.nextLong())
        worlds[Dimension.OVERWORLD] = world

        timer.scheduleAtFixedRate(LambdaTimerTask {
            GLOBAL!!.get().saveWorlds()
        }, 0, 60000)
    }

    @ExperimentalStdlibApi
    fun thread() {
        KettleProperties.load()
        initObjects()
        initWorlds()
        isFullyInitialized = true
        ConsoleLogger(KettleServer::class).info("Initialized")
        while(shouldTick()) {
            val start = Calendar.getInstance()
            tick()
            val end = Calendar.getInstance()
            val mil = (end[Calendar.MILLISECOND] - start[Calendar.MILLISECOND])
            Thread.sleep(max(50 - mil.toLong(), 0))
        }
    }

    private fun initObjects() {
        BLOCKS = HashMap()
        BLOCKS = Gson().fromJson(JavaServer::class.java.getResource("/blocks.json").openStream().reader(),
            BLOCKS.javaClass)
    }

    @ExperimentalStdlibApi
    fun onPlayerJoin(client: Client) {
        val world = GLOBAL!!.get().worlds[Dimension.OVERWORLD]!!
        val player = Player(client, world.newEntityId())
        world.entities.add(player)
        world.players.add(player)
        JavaServer.GLOBAL_CONTROLLER!!.get().execute {
            client.send(S2CUpdateViewPosition(0, 0))
            client.send(S2CDeclareRecipes())
            client.send(S2CDeclareTags())
            client.send(S2CHeldItemChange(0))
            client.send(S2CDeclareCommands())
            client.send(S2CUnlockRecipes(RecipeUnlockAction.EMPTY_INIT))
            client.send(S2CWorldBorderInit(1024.0))
            client.send(S2CSpawnPosition())
            client.send(S2CEntityStatus(player.eid, 23))
            client.send(S2CEntityStatus(player.eid, 28))
            client.send(S2CPlayerPosAndLook(
                KettleProperties.spawn.x, false,
                KettleProperties.spawn.y, false,
                KettleProperties.spawn.z, false,
                KettleProperties.spawn.yaw.toFloat(), false,
                KettleProperties.spawn.pitch.toFloat(), false,
            ))
        }
    }

    @ExperimentalStdlibApi
    fun saveWorlds() {
        val chunksList = ListTag(IntArrayTag::class.java)

        if(!File("worlds").exists()) File("worlds").mkdir()

        worlds.forEach { (dimension, world) ->
            logger.info("Saving world $dimension")
            val nbt = WorldNBTEncoder.encode(world, chunksList)
            NBTUtil.write(NamedTag("World", nbt), File("worlds/${dimension.name.toLowerCase()}.nbt"), true)
        }

        logger.info("Saving chunks")
        val chunkCompound = CompoundTag()
        chunkCompound.put("ChunkData", chunksList)
        NBTUtil.write(NamedTag("ChunkInfo", chunkCompound), File("worlds/chunks.nbt"), true)

        logger.info("All done!")
    }

    companion object {
        var GLOBAL: AtomicReference<KettleServer>? = AtomicReference(KettleServer())
        lateinit var THREAD: AtomicReference<Thread>
        lateinit var BLOCKS: HashMap<String, Int>
    }
}