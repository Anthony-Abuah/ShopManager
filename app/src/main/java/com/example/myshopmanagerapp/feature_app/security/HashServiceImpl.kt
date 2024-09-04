package com.example.myshopmanagerapp.feature_app.security

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.codec.binary.Hex
import java.security.SecureRandom

private const val SALT_ALGORITHM = "SHA1PRNG"
class HashServiceImpl : HashService {
    override suspend fun generateSaltedHash(value: String, saltLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance(SALT_ALGORITHM).generateSeed(saltLength)
        val saltAsHex = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex("$saltAsHex$value")
        return SaltedHash(
            hash = hash,
            salt = saltAsHex
        )
    }

    override suspend fun verify(password: String, salt: String, hashedPassword: String): Boolean {
        return DigestUtils.sha256Hex(salt + password) == hashedPassword
    }


}