package com.liamcoalstudio.kettle.logging

class BranchLogger(private vararg val logger: Logger) : Logger {
    override fun log(string: String) = logger.map { return it.log(string) }.reduce<Boolean, Boolean> { a, b -> a && b }
}