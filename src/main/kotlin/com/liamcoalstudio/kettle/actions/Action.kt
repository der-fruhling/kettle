package com.liamcoalstudio.kettle.actions

interface Action {
    fun perform()
    val fields: HashMap<String, Any>
}