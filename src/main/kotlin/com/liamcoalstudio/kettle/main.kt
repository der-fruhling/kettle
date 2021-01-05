package com.liamcoalstudio.kettle

import com.liamcoalstudio.kettle.bootstrap.Downloader
import com.liamcoalstudio.kettle.servers.java.JavaServer
import com.liamcoalstudio.kettle.servers.java.JavaServerController
import com.liamcoalstudio.kettle.servers.main.KettleServer
import java.util.concurrent.atomic.AtomicReference

fun main() {
    Downloader.downloadAll()

    KettleServer.THREAD = AtomicReference(Thread {
        KettleServer.GLOBAL = AtomicReference(KettleServer())
        KettleServer.GLOBAL.get().thread()
    })
    KettleServer.THREAD.get().name = "kettle.thread_tick"
    KettleServer.THREAD.get().start()

    JavaServer.GLOBAL_CONTROLLER = AtomicReference(JavaServer().start() as JavaServerController)
    JavaServer.GLOBAL_CONTROLLER!!.get().start()
    JavaServer.GLOBAL_CONTROLLER!!.get().thread.join()
}