package me.mapyt.app.platform.utils

import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets

object FileTestHelper {

    internal fun getFileStringContentFromResources(filePath: String): String? {
        val inputStream = javaClass.classLoader?.getResourceAsStream(filePath)
        val source = inputStream?.let { inputStream.source().buffer() }
        return source?.readString(StandardCharsets.UTF_8)
    }
}