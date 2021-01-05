package com.liamcoalstudio.kettle.networking.main.packets

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.networking.main.Client

abstract class Packet(val id: Int, val clientState: ClientState.Status?) {
    open fun read(buf: Buffer) {}
    open fun write(buf: Buffer) {}
    open fun updateOnRead(state: ServerState, client: Client) {}
    open fun updateOnWrite(state: ServerState, client: Client) {}
}