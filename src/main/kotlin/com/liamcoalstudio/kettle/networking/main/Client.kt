package com.liamcoalstudio.kettle.networking.main

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.logging.ConsoleLogger
import com.liamcoalstudio.kettle.networking.main.nodes.NodeManager
import com.liamcoalstudio.kettle.networking.main.packets.*
import com.liamcoalstudio.kettle.servers.java.JavaServer
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.util.concurrent.atomic.AtomicReference

class Client(val socketChannel: AsynchronousSocketChannel) {
    lateinit var thread: Thread
    private val socketChannelReference  = AtomicReference(socketChannel)
    var status                          = ClientState.Status.Handshake
    private val buffer                  = ByteBuffer.allocate(65535)
    private val nodeManager             = NodeManager()
    private val logger                  = ConsoleLogger(Client::class)

    fun write(packet: Buffer) = socketChannel.write(ByteBuffer.wrap(packet.array))

    fun start() {
        thread = Thread(this::thread)
        thread.name = this.toString()
        thread.start()
    }

    private fun thread() {
        socketChannelReference.get().read(buffer, AtomicReference(buffer), CompletionHandler(this::readFromThread))
    }

    private fun readFromThread(size: Int, buffer: AtomicReference<ByteBuffer>) {
        JavaServer.GLOBAL_CONTROLLER!!.get().execute {
            val buf = buffer.get().flip()
            val pbuf = Buffer()
            pbuf.array = nodeManager.putRead(buf.array().slice(0 until buf.limit()).toByteArray())
            while(pbuf.hasMore) {
                val length = pbuf.getVarInt()
                val sub = pbuf.getPacketBuffer(length)
                val id = sub.getVarInt()
                val packet = JavaPacket.fromIdAndState(status, id)
                if(packet == null) {
                    logger.error("$id wasn't found in $status")
                } else {
                    val state = JavaServer.GLOBAL.state
                    val pkt = packet.producer.produce(state)
                    pkt.read(sub)
                    pkt.updateOnRead(state, this)
                }
            }
        }
    }

    fun send(packet: Packet) {
        JavaServer.GLOBAL_CONTROLLER!!.get().execute {
            val data = Buffer()
            data.addVarInt(packet.id)
            packet.write(data)

            val output = Buffer()
            output.addVarInt(data.array.size)
            output.addPacketBuffer(data)

            JavaServer.GLOBAL_CONTROLLER!!.get().execute {
                packet.updateOnWrite(JavaServer.GLOBAL.state, this)
            }
            write(Buffer(nodeManager.putWrite(output.array)))
        }
    }

    private class CompletionHandler(val handler: (Int, AtomicReference<ByteBuffer>) -> Unit) : java.nio.channels.CompletionHandler<Int, AtomicReference<ByteBuffer>> {
        override fun completed(result: Int?, attachment: AtomicReference<ByteBuffer>?) {
            handler(result!!, attachment!!)
        }

        override fun failed(exc: Throwable?, attachment: AtomicReference<ByteBuffer>?) {
            TODO("not implemented")
        }
    }
}