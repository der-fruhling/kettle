package com.liamcoalstudio.kettle.bootstrap

import kotlinx.serialization.Serializable

@Serializable
data class DownloadEntry(
    var dest: String,
    var src: String
)
