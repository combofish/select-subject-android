package com.example.select.utils

import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils

/**
 * 常用  编码/解码  工具类
 *
 * @author Administrator
 */
object EncryptionUtils {
    //base64
    fun base64Encode(data: String): String {
        return Base64.encodeBase64String(data.toByteArray())
    }

    fun base64Encode(bytes: ByteArray?): String {
        return Base64.encodeBase64String(bytes)
    }

    fun base64Decode(data: String): ByteArray {
        return Base64.decodeBase64(data.toByteArray())
    }

    //MD5
    fun md5(data: String?): String {
        return DigestUtils.md5Hex(data)
    }

    //sha1
    fun sha1(data: String?): String {
        return DigestUtils.sha1Hex(data)
    }

    //sha256Hex
    fun sha256Hex(data: String?): String {
        return DigestUtils.sha256Hex(data)
    }
}