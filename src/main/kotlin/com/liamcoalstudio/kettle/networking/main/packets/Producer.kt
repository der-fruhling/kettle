package com.liamcoalstudio.kettle.networking.main.packets

interface Producer<T> {
    fun produce(serverState: ServerState): T
}