package com.liamcoalstudio.kettle.world

import net.querz.nbt.tag.CompoundTag

enum class Biome(
    val biomeName: String,
    val id: Int,
    val precipitation: String,
    val depth: Float,
    val temperature: Float,
    val scale: Float,
    val downfall: Float,
    val category: String,
    val skyColor: Int,
    val waterColor: Int,
    val fogColor: Int,
    val waterFogColor: Int,
    val moodSound: String,
    val delay: Int,
    val offset: Double,
    val blockSearchExtent: Int
) {
    OCEAN(
        biomeName = "minecraft:ocean",
        id = 0,
        precipitation = "rain",
        depth = -1.0f,
        temperature = 0.5f,
        scale = 0.1f,
        downfall = 0.5f,
        category = "ocean",
        skyColor = 8103167,
        waterColor = 4159204,
        fogColor = 12638463,
        waterFogColor = 329011,
        moodSound = "minecraft:ambient.cave",
        delay = 6000,
        offset = 2.0,
        blockSearchExtent = 8
    );
    val codec: CompoundTag get() {
        val compound = CompoundTag()
        compound.putString("name", biomeName)
        compound.putInt("id", id)
        val element = CompoundTag()
        element.putString("precipitation", precipitation)
        element.putFloat("depth", depth)
        element.putFloat("temperature", temperature)
        element.putFloat("scale", scale)
        element.putFloat("downfall", downfall)
        element.putString("category", category)
        val effects = CompoundTag()
        effects.putInt("sky_color", skyColor)
        effects.putInt("water_fog_color", waterFogColor)
        effects.putInt("fog_color", fogColor)
        effects.putInt("water_color", waterColor)
        val moodSoundTag = CompoundTag()
        moodSoundTag.putInt("tick_delay", delay)
        moodSoundTag.putDouble("offset", offset)
        moodSoundTag.putString("sound", moodSound)
        moodSoundTag.putInt("block_search_extent", blockSearchExtent)
        effects.put("mood_sound", moodSoundTag)
        element.put("effects", effects)
        compound.put("element", element)
        return compound
    }
}