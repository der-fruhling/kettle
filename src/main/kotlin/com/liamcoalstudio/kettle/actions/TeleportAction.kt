package com.liamcoalstudio.kettle.actions

import com.liamcoalstudio.kettle.networking.java.play.S2CUpdateViewPosition
import com.liamcoalstudio.kettle.servers.java.JavaServer
import com.liamcoalstudio.kettle.servers.main.KettleServer
import com.liamcoalstudio.kettle.world.Player
import kotlin.math.floor

class TeleportAction(val player: Player,
                     val x: Double, val y: Double, val z: Double,
                     val yaw: Float = 0.0f, val pitch: Float = 0.0f) : Action {
    @ExperimentalStdlibApi
    override fun perform() {
        KettleServer.GLOBAL!!.get().execute {
            player.x = x
            player.y = y
            player.z = z
            player.yaw = yaw
            player.pitch = pitch
        }
        player.client!!.send(S2CUpdateViewPosition(floor(x / 16.0).toInt(), floor(z / 16.0).toInt()))
        player.updateChunks().start()
    }

    override val fields: HashMap<String, Any>
        get() = createFieldsVariable()

    private fun createFieldsVariable(): HashMap<String, Any> {
        val a = HashMap<String, Any>()
        a["type"] = TeleportAction::class.java
        a["x"] = x
        a["y"] = y
        a["z"] = z
        a["yaw"] = yaw
        a["pitch"] = pitch
        return a
    }
}