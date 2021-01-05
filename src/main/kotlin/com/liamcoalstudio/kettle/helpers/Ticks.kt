package com.liamcoalstudio.kettle.helpers

import kotlin.math.floor

object Ticks {
    fun fromSeconds(seconds: Float) = floor(seconds * 20f)
    fun fromMinutes(minutes: Float) = floor(fromSeconds(minutes) * 60f)
    fun fromHours(hours: Float) = floor(fromMinutes(hours) * 60f)
    fun fromDays(days: Float) = floor(fromHours(days) * 24f)
}