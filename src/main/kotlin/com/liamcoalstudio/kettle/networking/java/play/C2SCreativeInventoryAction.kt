package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.helpers.Dimension
import com.liamcoalstudio.kettle.helpers.Item
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.ClientState
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.networking.main.packets.Producer
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.servers.main.KettleServer
import com.liamcoalstudio.kettle.world.Slot
import kotlin.properties.Delegates

class C2SCreativeInventoryAction : Packet(0x28, ClientState.Status.Play), Producer<Packet> {
    var slot by Delegates.notNull<Short>()
    lateinit var clickedItem: Slot

    override fun read(buf: Buffer) {
        slot = buf.getShort()
        clickedItem = Slot.read(buf)
    }

    override fun updateOnRead(state: ServerState, client: Client) {
        val player = KettleServer.player(client)
        if(clickedItem.present && Item.values().contains(clickedItem.item!!))
            player.inventory[slot.toInt()] = clickedItem
        else if(clickedItem.present)
            player.inventory[slot.toInt()]
    }

    override fun produce(serverState: ServerState) = C2SCreativeInventoryAction()
}