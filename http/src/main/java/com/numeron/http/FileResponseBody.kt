package com.numeron.http

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.BufferedSource
import java.io.File


internal class FileResponseBody(private val delegate: ResponseBody, val file: File) : ResponseBody() {

    override fun source(): BufferedSource = delegate.source()

    override fun contentLength(): Long = delegate.contentLength()

    override fun contentType(): MediaType? = delegate.contentType()

}