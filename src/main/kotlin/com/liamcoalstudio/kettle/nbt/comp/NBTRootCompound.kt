package com.liamcoalstudio.kettle.nbt.comp

import com.liamcoalstudio.kettle.nbt.NBTTag

class NBTRootCompound : NBTCompound() {

    override fun nameOf(value: NBTTag): String = "_"
}