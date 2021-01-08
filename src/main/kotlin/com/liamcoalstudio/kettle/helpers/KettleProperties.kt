package com.liamcoalstudio.kettle.helpers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import java.io.FileReader
import java.io.FileWriter
import kotlin.properties.Delegates

object KettleProperties {
    lateinit var motd: String
    var viewDistance by Delegates.notNull<Int>()
    var gamemode by Delegates.notNull<Int>()
    lateinit var spawn: ConfiguredSpawnLocation

    fun load() {
        val gson = Gson()
        val p = gson.fromJson(FileReader("properties.json"), GsonProperties::class.java)
        motd = p.motd
        viewDistance = p.viewDistance
        gamemode = p.gamemode
        spawn = p.spawn
    }
}