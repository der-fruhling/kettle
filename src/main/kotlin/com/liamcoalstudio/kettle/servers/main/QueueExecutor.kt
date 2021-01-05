package com.liamcoalstudio.kettle.servers.main

import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicReference

class QueueExecutor : Executor {
    private val queue: AtomicReference<Queue<Runnable>> = AtomicReference(LinkedList())

    @Synchronized
    override fun execute(command: Runnable) {
        queue.getAndUpdate {
            it.offer(command)
            it
        }
    }

    @Synchronized
    fun run() {
        val a = queue.get()
        while(a.peek() != null)
            a.poll().run()
        queue.get().clear()
    }
}