package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.helpers.RecipeUnlockAction
import com.liamcoalstudio.kettle.networking.main.packets.Packet

class S2CUnlockRecipes(private val r: RecipeUnlockAction) : Packet(0x35, null) {
    override fun write(buf: Buffer) {
        buf.addVarInt(r.type.action)
        buf.addBoolean(r.displayState.craftingMenuBookOpen)
        buf.addBoolean(r.displayState.craftingMenuBookFiltering)
        buf.addBoolean(r.displayState.furnaceBookOpen)
        buf.addBoolean(r.displayState.furnaceBookFiltering)
        buf.addBoolean(r.displayState.blastFurnaceBookOpen)
        buf.addBoolean(r.displayState.blastFurnaceBookFiltering)
        buf.addBoolean(r.displayState.smokerBookOpen)
        buf.addBoolean(r.displayState.smokerBookFiltering)
        buf.addVarInt(r.a1.size)
        r.a1.forEach { buf.addString(it.toString()) }
        if(r.type == RecipeUnlockAction.Type.Init) {
            buf.addVarInt(r.a2.size)
            r.a2.forEach { buf.addString(it.toString()) }
        }
    }
}