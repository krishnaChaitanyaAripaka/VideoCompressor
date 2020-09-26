package com.chaitu.videocompressor.standard

fun String.getBitrate(): String {
    val startString = "bitrate"
    val endString = "bits/s"
    if (contains(startString) && contains(endString)) {
        val startIndex = indexOf(startString) + startString.length
        val endIndex = indexOf(endString)

        return substring(startIndex, endIndex + endString.length).replace(" ", "").replace("=", "")
    }

    return ""
}

fun String.getSize(): String {
    val startString = "size"
    val endString = "kB "
    if (contains(startString) && contains(endString)) {
        val startIndex = indexOf(startString) + startString.length
        val endIndex = indexOf(endString)

        return substring(startIndex, endIndex + endString.length).replace(" ", "").replace("=", "")
    }

    return ""
}