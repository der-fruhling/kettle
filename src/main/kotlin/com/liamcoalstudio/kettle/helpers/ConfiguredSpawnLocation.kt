package com.liamcoalstudio.kettle.helpers

import kotlinx.serialization.Serializable

@Serializable
data class ConfiguredSpawnLocation(
    var x: Double = 0.0,
    var y: Double = 256.0,
    var z: Double = 0.0,
    var yaw: Double = 0.0,
    var pitch: Double = 0.0,
    var dimension: String = "Overworld"
)
