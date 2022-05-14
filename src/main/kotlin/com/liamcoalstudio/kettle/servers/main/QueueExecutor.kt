package com.liamcoalstudio.kettle.servers.main

import java.util.*
import java.util.concurrent.Executor

class QueueExecutor : Executor {
    @Volatile
    private var queue: Queue<Runnable> = LinkedList()

    @Synchronized
    override fun execute(command: Runnable) {
        queue.offer(command)
    }

    @Synchronized
    fun run() {
        while (queue.peek() != null)
            queue.poll().run()
    }
}