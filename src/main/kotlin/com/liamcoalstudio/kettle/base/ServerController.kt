package com.liamcoalstudio.kettle.base

abstract class ServerController(private val server: Server) {
    abstract val thread: Thread

    fun start() = thread.start()
    fun stopForcefully() = thread.interrupt()
    fun execute(runnable: Runnable) = server.executor.execute(runnable)

    fun stop() {
        server.deinit()
        thread.join(30000)
        if (thread.isAlive)
            stopForcefully()
    }
}
