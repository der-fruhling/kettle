package com.liamcoalstudio.kettle.helpers

enum class Item(val id: Int, val block: Block? = null) {
    GrassBlock(8, Block.GrassBlock),
    Stone(1, Block.Stone),
    Dirt(9, Block.Dirt),
    Bedrock(29, Block.Bedrock),
}
