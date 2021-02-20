package me.mapyt.app.platform.utils

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets

internal fun MockWebServer.enqueueResponse(filePath: String, code: Int) {
    val fileContent = FileTestHelper.getFileStringContentFromResources(filePath)
        ?: throw IllegalArgumentException("filePath")
    enqueue(
        MockResponse()
            .setResponseCode(code)
            .setBody(fileContent)
    )
}