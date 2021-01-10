package com.liamcoalstudio.kettle.networking.main.packets

import com.liamcoalstudio.kettle.networking.java.handshake.C2SHandshakePacket
import com.liamcoalstudio.kettle.networking.java.login.C2SLoginStartPacket
import com.liamcoalstudio.kettle.networking.java.play.*
import com.liamcoalstudio.kettle.networking.java.status.C2SPingPacket
import com.liamcoalstudio.kettle.networking.java.status.C2SStatusRequestPacket
import com.liamcoalstudio.kettle.networking.main.packets.ClientState.Status.*

enum class JavaPacket(val clientState: ClientState.Status, val id: Int, val producer: Producer<Packet>) {
    HHandshakePacket(Handshake, 0x00, C2SHandshakePacket()),
    SStatusRequestPacket(Status, 0x00, C2SStatusRequestPacket(null)),
    SPingPacket(Status, 0x01, C2SPingPacket()),
    LLoginStart(Login, 0x00, C2SLoginStartPacket()),
    PClientStatus(Play, 0x04, C2SClientStatus()),
    PTeleportOK(Play, 0x00, C2STeleportConfirm()),
    PPlayerRP(Play, 0x13, C2SPlayerPosAndRot()),
    PPlayerR(Play, 0x14, C2SPlayerRot()),
    PPlayerP(Play, 0x12, C2SPlayerPos()),
    PClientSettings(Play, 0x05, C2SClientSettings()),
    PBlockPlace(Play, 0x2e, C2SBlockPlacement()),
    PCreativeInventoryAction(Play, 0x28, C2SCreativeInventoryAction()),

    ;

    companion object {
        fun fromIdAndState(state: ClientState.Status, id: Int): JavaPacket? {
            return values().find { it.clientState == state && it.id == id }
        }
    }
}