package com.liamcoalstudio.kettle.tasks

import java.util.concurrent.atomic.AtomicInteger

/**
 * Manages tasks
 */
object Tasks {
    /**
     * Executes all tasks in [runnables] synchronously.
     *
     * @param runnables Tasks to execute
     */
    fun executeAll(vararg runnables: Runnable) {
        runnables.forEach { it.run() }
    }

    /**
     * Executes all tasks in [runnables] asynchronously.
     *
     * @param runnables Tasks to execute
     * @return [AwaitableTask]
     */
    fun executeAllAsync(vararg runnables: Runnable): AwaitableTask {
        val ref = runnables.toMutableList()
        val j = AtomicInteger()
        val threads = MutableList(Runtime.getRuntime().availableProcessors()) { i ->
            Thread({
                while (ref.isNotEmpty()) {
                    var r: Runnable
                    try {
                        r = ref[j.getAndIncrement()]
                    } catch (e: IndexOutOfBoundsException) {
                        return@Thread
                    }
                    r.run()
                }
            }, "WorkerThread:$i")
        }
        threads.forEach { it.start() }
        return AwaitableTask(threads.toTypedArray())
    }
}