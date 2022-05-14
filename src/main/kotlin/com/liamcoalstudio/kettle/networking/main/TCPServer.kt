package com.liamcoalstudio.kettle.networking.main

import com.liamcoalstudio.kettle.logging.ConsoleLogger
import java.net.InetSocketAddress
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel

class TCPServer(port: Int) {
    private val logger = ConsoleLogger(TCPServer::class)
    private val socketChannel: AsynchronousServerSocketChannel =
        AsynchronousServerSocketChannel.open().bind(InetSocketAddress(port))

    class CompletionHandler(val handler: (TCPServer, Client) -> Unit, private val server: TCPServer) :
        java.nio.channels.CompletionHandler<AsynchronousSocketChannel, TCPServer> {
        override fun completed(p0: AsynchronousSocketChannel?, p1: TCPServer?) {
            server.logger.info("Client connected")
            handler(p1!!, Client(p0!!))
        }

        override fun failed(p0: Throwable?, p1: TCPServer?) {
            p0?.printStackTrace()
        }
    }

    fun destroy() {
        socketChannel.close()
    }

    fun accept(handler: (TCPServer, Client) -> Unit) {
        socketChannel.accept(this, CompletionHandler(handler, this))
    }
}