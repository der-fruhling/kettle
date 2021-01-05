package com.liamcoalstudio.kettle.bootstrap

import com.google.gson.Gson
import java.io.File
import java.net.URL
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel
import java.nio.channels.FileChannel

import java.io.FileOutputStream

object Downloader {
    fun download(dest: String, url: URL) {
        val ch: ReadableByteChannel = Channels.newChannel(url.openStream())
        val fos = FileOutputStream(dest)
        val out: FileChannel = fos.channel
        out.transferFrom(ch, 0, Long.MAX_VALUE)
        out.close()
        ch.close()
    }

    fun downloadAll() {
        val res = Downloader::class.java.getResource("/objects/download.json")
        val entries = Gson().fromJson(res.openStream().reader(), Array<DownloadEntry>::class.java)
        if(!File("objects").exists())
            File("objects").mkdir()
        entries.forEach {
            download("objects/${it.dest}", URL(it.src))
        }
    }
}
