package com.liamcoalstudio.kettle.servers.java

import com.liamcoalstudio.kettle.base.Server
import com.liamcoalstudio.kettle.base.ServerController
import com.liamcoalstudio.kettle.helpers.Dimension
import com.liamcoalstudio.kettle.logging.ConsoleLogger
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.TCPServer
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.servers.main.KettleServer
import com.liamcoalstudio.kettle.servers.main.QueueExecutor
import com.liamcoalstudio.kettle.world.Player
import java.lang.Exception
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicReference

class JavaServer : Server {
    private val logger = ConsoleLogger(JavaServer::class)
    override val platformName: String = "java"

    private var iExecutor = QueueExecutor()
    override val executor: Executor = iExecutor

    lateinit var server: TCPServer

    override fun init(): Boolean {
        Thread {
            executor.execute {
                logger.info("yay")
            }
        }.start()
        val init = initObjects() &&
                   initNetworking()
        server.accept(this::clientConnected)
        GLOBAL = this
        return init
    }

    override fun deinit(): Boolean {
        server.destroy()
        return true
    }

    override fun tick() {
        tickNetworking()
        try {
            iExecutor.run()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun shouldTick(): Boolean {
        return true
    }

    override fun createServerController(thread: Thread): ServerController {
        return JavaServerController(this, thread)
    }

    private fun initObjects(): Boolean {

        return true
    }

    private fun initNetworking(): Boolean {
        server = TCPServer(25565)
        return true
    }

    private fun tickNetworking() {
        if(KettleServer.GLOBAL!!.get().isFullyInitialized)
            KettleServer.GLOBAL!!.get().worlds[Dimension.OVERWORLD]!!.players.forEach { it.client!!.tick() }
    }

    private fun clientConnected(server: TCPServer, client: Client) {
        client.start()
        server.accept(this::clientConnected)
    }

    val state: ServerState get() {
        return ServerState(this)
    }

    companion object {
        const val PROTOCOL_NUMBER = 754
        var GLOBAL_CONTROLLER: AtomicReference<JavaServerController>? = null
        lateinit var GLOBAL: JavaServer
    }
}