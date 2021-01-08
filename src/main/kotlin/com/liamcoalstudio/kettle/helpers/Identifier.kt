package com.liamcoalstudio.kettle.helpers

data class Identifier(val namespace: String, val value: String) {
    override fun toString(): String {
        return "$namespace:$value"
    }
}
