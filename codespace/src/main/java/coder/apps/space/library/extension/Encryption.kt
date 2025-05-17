package coder.apps.space.library.extension

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val SECRET_KEY = "aesEncryptionKey"
private const val INIT_VECTOR = "encryptionIntVec"
private const val ALGORITHM = "AES/CBC/PKCS5PADDING"

private fun initCipher(mode: Int, secretKey: String? = null): Cipher =
    Cipher.getInstance(ALGORITHM).apply {
        init(
            mode,
            SecretKeySpec((secretKey ?: SECRET_KEY).toByteArray(Charsets.UTF_8), "AES"),
            IvParameterSpec(INIT_VECTOR.toByteArray(Charsets.UTF_8))
        )
    }

/**
 * Encrypts a string using AES/CBC/PKCS5Padding.
 * @param value The string to encrypt.
 * @param secretKey Optional key; defaults to internal key if null.
 * @return Base64-encoded encrypted string, or null if encryption fails.
 */
fun encrypt(value: String, secretKey: String? = null): String? = try {
    Base64.encodeToString(
        initCipher(Cipher.ENCRYPT_MODE, secretKey).doFinal(value.toByteArray()),
        Base64.DEFAULT
    )
} catch (e: Exception) {
    null
}

/**
 * Decrypts a Base64-encoded AES/CBC/PKCS5Padding-encrypted string.
 * @param value The Base64-encoded encrypted string.
 * @param secretKey Optional key; defaults to internal key if null.
 * @return Decrypted string, or null if decryption fails.
 */
fun decrypt(value: String?, secretKey: String? = null): String? = try {
    value?.let {
        String(
            initCipher(Cipher.DECRYPT_MODE, secretKey).doFinal(
                Base64.decode(
                    it,
                    Base64.DEFAULT
                )
            )
        )
    }
} catch (e: Exception) {
    null
}