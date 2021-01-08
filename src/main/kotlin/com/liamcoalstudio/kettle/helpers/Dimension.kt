package com.liamcoalstudio.kettle.helpers

import net.querz.nbt.tag.CompoundTag

enum class Dimension(
    val dimName: String,
    val id: Int,
    val piglinSafe: Boolean,
    val natural: Boolean,
    val ambientLight: Float,
    val infiniburnTag: String,
    val respawnAnchorWorks: Boolean,
    val hasSkylight: Boolean,
    val bedWorks: Boolean,
    val effectsId: String,
    val hasRaids: Boolean,
    val logicalHeight: Int,
    val coordinateScale: Double,
    val ultrawarm: Boolean,
    val hasCeiling: Boolean
) {
    OVERWORLD(
        dimName = "minecraft:overworld",
        id = 0,
        piglinSafe = false,
        natural = true,
        ambientLight = 0.0f,
        infiniburnTag = "minecraft:infiniburn_overworld",
        respawnAnchorWorks = false,
        hasSkylight = true,
        bedWorks = true,
        effectsId = "minecraft:overworld",
        hasRaids = true,
        logicalHeight = 256,
        coordinateScale = 1.0,
        ultrawarm = false,
        hasCeiling = false
    );

    val codec: CompoundTag
    get() {
        val compound = CompoundTag()
        compound.putString("name", dimName)
        compound.putInt("id", id)
        val element = CompoundTag()
        element.putBoolean("piglin_safe", piglinSafe)
        element.putBoolean("natural", natural)
        element.putFloat("ambient_light", ambientLight)
        element.putString("infiniburn", infiniburnTag)
        element.putBoolean("respawn_anchor_works", respawnAnchorWorks)
        element.putBoolean("has_skylight", hasSkylight)
        element.putBoolean("bed_works", bedWorks)
        element.putString("effects", effectsId)
        element.putBoolean("has_raids", hasRaids)
        element.putInt("logical_height", logicalHeight)
        element.putDouble("coordinate_scale", coordinateScale)
        element.putBoolean("ultrawarm", ultrawarm)
        element.putBoolean("has_ceiling", hasCeiling)
        compound.put("element", element)
        return compound
    }
}