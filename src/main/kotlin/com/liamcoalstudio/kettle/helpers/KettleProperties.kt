package com.liamcoalstudio.kettle.helpers

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.FileReader
import java.io.FileWriter
import kotlin.properties.Delegates

object KettleProperties {
    private class GsonProperties {
        @JvmField var motd: String = "A tea cup transmitting over the\ninternet."
        @JvmField var viewDistance: Int = 8
        @JvmField var gamemode: Int = 0
    }

    lateinit var motd: String
    var viewDistance by Delegates.notNull<Int>()
    var gamemode by Delegates.notNull<Int>()

    fun load() {
        val gson = Gson()
        val p = gson.fromJson(FileReader("properties.json"), GsonProperties::class.java)
        motd = p.motd
        viewDistance = p.viewDistance
        gamemode = p.gamemode
    }

    fun save() {
        val gson = Gson()
        val p = GsonProperties()
        p.motd = motd
        p.viewDistance = viewDistance
        p.gamemode = gamemode
        gson.toJson(p, FileWriter("properties.json"))
    }
}