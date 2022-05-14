package com.liamcoalstudio.kettle.servers.java

import com.liamcoalstudio.kettle.base.ServerController

class JavaServerController(server: JavaServer, override val thread: Thread) : ServerController(server)
