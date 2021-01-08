package com.liamcoalstudio.kettle.servers.main

import com.google.gson.Gson
import com.liamcoalstudio.kettle.helpers.Block
import com.liamcoalstudio.kettle.helpers.Dimension
import com.liamcoalstudio.kettle.helpers.RecipeUnlockAction
import com.liamcoalstudio.kettle.logging.ConsoleLogger
import com.liamcoalstudio.kettle.networking.java.play.*
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.servers.java.JavaServer
import com.liamcoalstudio.kettle.world.Chunk
import com.liamcoalstudio.kettle.world.Player
import com.liamcoalstudio.kettle.world.Position
import com.liamcoalstudio.kettle.world.World
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.max
import kotlin.random.Random

class KettleServer {
    var isFullyInitialized: Boolean = false
    private val executor = QueueExecutor()

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
        val world = World.noise(Random.nextLong())
        worlds[Dimension.OVERWORLD] = world
    }

    @ExperimentalStdlibApi
    fun thread() {
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
        BLOCKS = Gson().fromJson(JavaServer::class.java.getResource("/objects/blocks.json").openStream().reader(),
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
                0.0, false,
                256.0, false,
                0.0, false,
                0.0f, false,
                0.0f, false,
            ))
        }
    }

    companion object {
        var GLOBAL: AtomicReference<KettleServer>? = AtomicReference(KettleServer())
        lateinit var THREAD: AtomicReference<Thread>
        lateinit var BLOCKS: HashMap<String, Int>
    }
}