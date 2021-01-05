package com.liamcoalstudio.kettle.networking.main.packets

data class ClientState(var status: Status) {
    enum class Status {
        Handshake,
        Status,
        Login,
        Play
    }
}