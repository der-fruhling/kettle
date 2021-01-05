package com.liamcoalstudio.kettle.servers.main

import com.liamcoalstudio.kettle.helpers.Block
import com.liamcoalstudio.kettle.helpers.Dimension
import com.liamcoalstudio.kettle.world.Position
import com.liamcoalstudio.kettle.world.World
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.max

class KettleServer {
    private val executor = QueueExecutor()

    val worlds = EnumMap<Dimension, World>(Dimension::class.java)

    fun execute(runnable: Runnable) {
        executor.execute(runnable)
    }

    private fun tick() {
        executor.run()
    }

    private fun shouldTick(): Boolean = true

    private fun initWorlds() {
        worlds[Dimension.OVERWORLD] = World()
        execute { worlds[Dimension.OVERWORLD]!![Position(64, 64, 64)] = Block.stone }
    }

    fun thread() {
        initWorlds()
        while(shouldTick()) {
            val start = Calendar.getInstance()
            tick()
            val end = Calendar.getInstance()
            val mil = (end[Calendar.MILLISECOND] - start[Calendar.MILLISECOND])
            Thread.sleep(max(50 - mil.toLong(), 0))
        }
    }

    companion object {
        lateinit var GLOBAL: AtomicReference<KettleServer>
        lateinit var THREAD: AtomicReference<Thread>
    }
}