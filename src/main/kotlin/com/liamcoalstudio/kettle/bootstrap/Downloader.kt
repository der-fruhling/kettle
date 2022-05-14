package com.liamcoalstudio.kettle.bootstrap

import com.liamcoalstudio.kettle.logging.ConsoleLogger
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel

object Downloader {
    val logger = ConsoleLogger(Downloader::class)

    fun download(dest: String, url: URL) {
        val ch: ReadableByteChannel = Channels.newChannel(url.openStream())
        val fos = FileOutputStream(dest)
        val out: FileChannel = fos.channel
        out.transferFrom(ch, 0, Long.MAX_VALUE)
        out.close()
        ch.close()
    }

    fun downloadAll() {
        val res = Downloader::class.java.getResource("/download.json")
        val entries = Json.decodeFromStream<List<DownloadEntry>>(res!!.openStream())
        if (!File("objects").exists())
            File("objects").mkdir()
        val toDownload = entries.filter { !File("objects/${it.dest}").exists() }
        toDownload.forEach {
            download("objects/${it.dest}", URL(it.src))
        }
        if (toDownload.isEmpty())
            logger.info("All up to date :D")
        else
            logger.info("Downloaded ${toDownload.size} files.")
    }
}
