package com.github.eduardocodigo0.springbootnotes.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class HashEncoder {
    private val bCrypt = BCryptPasswordEncoder()

    fun encode(rawString: String): String = bCrypt.encode(rawString)

    fun matches(raw: String, encoded: String): Boolean = bCrypt.matches(raw, encoded)
}