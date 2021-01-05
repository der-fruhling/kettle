package com.liamcoalstudio.kettle.base

import java.util.*
import java.util.concurrent.Executor
import kotlin.math.max

interface Server {
    val platformName: String
    val executor: Executor

    fun init(): Boolean
    fun deinit(): Boolean
    fun tick()
    fun shouldTick(): Boolean
    fun createServerController(thread: Thread): ServerController

    fun start(): ServerController {
        val t = Thread {
            init()
            while(shouldTick()) {
                val start = Calendar.getInstance()
                tick()
                val end = Calendar.getInstance()
                val mil = (end[Calendar.MILLISECOND] - start[Calendar.MILLISECOND])
                Thread.sleep(max(50 - mil.toLong(), 0))
            }
        }
        t.name = "${platformName}.thread_server"
        return createServerController(t)
    }
}