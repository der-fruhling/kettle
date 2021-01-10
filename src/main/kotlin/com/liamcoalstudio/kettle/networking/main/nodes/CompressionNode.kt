package com.liamcoalstudio.kettle.networking.main.nodes

import com.liamcoalstudio.kettle.helpers.Buffer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterOutputStream

class CompressionNode : Node() {
    var threshold = 0

    override fun passWrite(input: ByteArray): ByteArray {
        val bufi = Buffer(input)
        val size = bufi.getVarInt()
        return if(threshold in 1..size) {
            val out = ByteArrayOutputStream()
            val bytes = DeflaterOutputStream(out)

            val ub = bufi.getBuffer(size).array
            bytes.write(ub)
            bytes.finish()
            bytes.close()

            val buf = Buffer()
            buf.addVarInt(size)
            buf.addBuffer(Buffer(out.toByteArray()))

            val bufo = Buffer()
            bufo.addVarInt(buf.array.size)
            bufo.addBuffer(buf)
            bufo.array
        } else {
            val buffer1 = Buffer()
            buffer1.addVarInt(0)
            buffer1.addBuffer(bufi.getBuffer(size))

            val buffer = Buffer()
            buffer.addVarInt(buffer1.array.size)
            buffer.addBuffer(buffer1)
            buffer.array
        }
    }

    override fun passRead(input: ByteArray): ByteArray {
        val bufi = Buffer(input)
        val size = bufi.getVarInt()
        val buf = bufi.getBuffer(size)
        val usize = buf.getVarInt()
        val bytesLeft = size - (size - buf.bytesLeft)
        return if(usize > 0) {
            val compressed = buf.getBuffer(bytesLeft).array

            println(compressed.contentToString())
            val out = ByteArrayOutputStream()
            val inflater = InflaterOutputStream(out)
            inflater.write(compressed)
            inflater.finish()
            inflater.close()

            val outbuf = Buffer()
            val outbytes = out.toByteArray()
            outbuf.addVarInt(outbytes.size)
            outbuf.addBuffer(Buffer(outbytes))
            outbuf.array
        } else {
            val outbuf = Buffer()
            outbuf.addVarInt(bytesLeft)
            outbuf.addBuffer(buf.getBuffer(bytesLeft))
            outbuf.array
        }
    }
}