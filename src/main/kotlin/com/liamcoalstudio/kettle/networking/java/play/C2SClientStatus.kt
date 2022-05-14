package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.ClientState
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.networking.main.packets.Producer
import com.liamcoalstudio.kettle.networking.main.packets.ServerState

class C2SClientStatus : Packet(0x04, ClientState.Status.Play), Producer<Packet> {
    var enum: Int = 0

    override fun read(buf: Buffer) {
        enum = buf.getVarInt()
    }

    @ExperimentalStdlibApi
    override fun updateOnRead(state: ServerState, client: Client) {
//        when(enum) {
//            0 -> {
//
//            }
//        }
    }

    override fun produce(serverState: ServerState) = C2SClientStatus()
}