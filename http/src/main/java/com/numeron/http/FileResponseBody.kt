package com.numeron.http

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okio.BufferedSource
import okio.buffer
import okio.source
import java.io.File


internal class FileResponseBody(val file: File, private val contentType: String?) : ResponseBody() {

    override fun contentLength(): Long = file.length()

    override fun source(): BufferedSource = file.source().buffer()

    override fun contentType(): MediaType? = contentType?.toMediaTypeOrNull()

}