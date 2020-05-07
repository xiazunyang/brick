package com.numeron.stateful.livedata

class StatefulException(override val message: String, cause: Throwable? = null) : RuntimeException(message, cause)