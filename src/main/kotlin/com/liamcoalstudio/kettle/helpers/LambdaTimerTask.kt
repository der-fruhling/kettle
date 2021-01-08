package com.liamcoalstudio.kettle.helpers

import java.util.*

class LambdaTimerTask(private val runnable: Runnable) : TimerTask() {
    override fun run() = runnable.run()
}