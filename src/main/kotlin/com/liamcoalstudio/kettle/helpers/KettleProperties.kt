package com.liamcoalstudio.kettle.helpers

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.intellij.lang.annotations.Language
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import kotlin.properties.Delegates

object KettleProperties {
    lateinit var motd: String
    var viewDistance by Delegates.notNull<Int>()
    var gamemode by Delegates.notNull<Int>()
    lateinit var spawn: ConfiguredSpawnLocation
    var flat by Delegates.notNull<Boolean>()

    @Language("json")
    private val defaultConfig = """
        {
          "motd": "Kettle Server!",
          "viewDistance": 4,
          "gamemode": 1,
          "flat": true,
          "spawn": {
            "x": 0.0,
            "y": 4.0,
            "z": 0.0
          }
        }
    """.trimIndent()

    fun load() {
        if (!File("properties.json").exists()) {
            val wr = FileWriter("properties.json")
            wr.write(defaultConfig)
            wr.close()
        }
        val p = FileReader("properties.json").use {
            Json.decodeFromString<ServerConfiguration>(
                serializer(),
                it.readText()
            )
        }
        motd = p.motd
        viewDistance = p.viewDistance
        gamemode = p.gamemode
        spawn = p.spawn
        flat = p.flat
    }
}