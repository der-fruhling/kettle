package com.liamcoalstudio.kettle.logging

interface Logger {
    fun log(string: String): Boolean
    fun info(string: String) = log("[info] $string")
    fun warning(string: String) = log("[warning] $string")
    fun error(string: String) = log("[error] $string")
    fun fatal(string: String) = log("[fatal] $string")
}