package com.liamcoalstudio.kettle.networking.main.packets

import com.liamcoalstudio.kettle.networking.java.handshake.C2SHandshakePacket
import com.liamcoalstudio.kettle.networking.java.login.C2SLoginStartPacket
import com.liamcoalstudio.kettle.networking.java.status.C2SStatusRequestPacket
import com.liamcoalstudio.kettle.networking.main.packets.ClientState.Status.*

enum class JavaPacket(val clientState: ClientState.Status, val id: Int, val producer: Producer<Packet>) {
    HHandshakePacket(Handshake, 0x00, C2SHandshakePacket()),
    SStatusRequestPacket(Status, 0x00, C2SStatusRequestPacket(null)),
    LLoginStart(Login, 0x00, C2SLoginStartPacket());

    companion object {
        fun fromIdAndState(state: ClientState.Status, id: Int): JavaPacket? {
            return values().find { it.clientState == state && it.id == id }
        }
    }
}