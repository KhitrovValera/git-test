package com.example.gittest.data.mapper

import android.util.Base64
import com.example.gittest.data.remote.model.ReadMeDto


fun ReadMeDto.toDomain(): String {
    return if (this.encoding != "base64") {
        this.content
    } else {
        try {
            val decodedBytes = Base64.decode(this.content, Base64.DEFAULT)
            String(decodedBytes, Charsets.UTF_8)
        } catch (_: Exception) {
            ""
        }
    }
}