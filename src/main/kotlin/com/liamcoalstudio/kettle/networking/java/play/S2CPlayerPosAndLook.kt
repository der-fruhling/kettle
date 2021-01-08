package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.actions.TeleportAction
import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.helpers.Dimension
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.servers.main.KettleServer
import com.liamcoalstudio.kettle.world.Player
import com.liamcoalstudio.kettle.world.World
import kotlin.random.Random

class S2CPlayerPosAndLook(
    val x: Double, val xr: Boolean,
    val y: Double, val yr: Boolean,
    val z: Double, val zr: Boolean,
    val yaw: Float, val yawr: Boolean,
    val pitch: Float, val pitchr: Boolean,
) : Packet(0x34, null) {
    var tid: Int = 0
    override fun write(buf: Buffer) {
        buf.addDouble(x)
        buf.addDouble(y)
        buf.addDouble(z)
        buf.addFloat(yaw)
        buf.addFloat(pitch)
        buf.addByte((0 or
            (if(xr)     0b00000001 else 0) or
            (if(yr)     0b00000010 else 0) or
            (if(zr)     0b00000100 else 0) or
            (if(pitchr) 0b00001000 else 0) or
            (if(yawr)   0b00010000 else 0)
        ).toByte())
        val id = Random.nextInt()
        buf.addVarInt(id)
        tid = id
    }

    private fun toAbsolute(player: Player): S2CPlayerPosAndLook = S2CPlayerPosAndLook(
        if(xr)x+player.x else x, xr,
        if(yr)y+player.y else y, yr,
        if(zr)z+player.z else z, zr,
        if(yawr)yaw+player.yaw else yaw, yawr,
        if(pitchr)pitch+player.pitch else pitch, pitchr
    )

    override fun updateOnWrite(state: ServerState, client: Client) {
        val player = KettleServer.GLOBAL!!.get().worlds[Dimension.OVERWORLD]!!.players.find { it.client == client }!!
        val abs = toAbsolute(player)
        player.actions[tid] = TeleportAction(player, abs.x, abs.y, abs.z, abs.yaw, abs.pitch)
    }
}