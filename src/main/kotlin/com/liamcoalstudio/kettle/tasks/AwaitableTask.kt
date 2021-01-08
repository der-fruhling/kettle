package com.liamcoalstudio.kettle.tasks

/**
 * A bunch of [threads] that can be [await]ed or [cancel]led.
 *
 * @author Liam Cole
 * @since 0.0000
 */
class AwaitableTask(private val threads: Array<Thread>) {
    /**
     * Waits for all [threads] to complete.
     *
     * @author Liam Cole
     * @since 0.0000
     *
     * @see Thread.join
     */
    fun await() = threads.forEach { it.join() }

    /**
     * Interrupts all [threads].
     *
     * @author Liam Cole
     * @since 0.0000
     *
     * @see Thread.interrupt
     */
    fun cancel() = threads.forEach { it.interrupt() }
}