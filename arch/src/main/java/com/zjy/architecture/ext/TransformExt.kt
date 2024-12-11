package com.zjy.architecture.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * @author zhengjy
 * @since 2020/05/21
 * Description:
 */
private val HEX_DIGITS =
    charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

/**
 * byte array to hex string
 */
fun ByteArray.bytes2Hex(): String {
    val result = CharArray(size shl 1)
    var index = 0
    for (b in this) {
        result[index++] = HEX_DIGITS[b.toInt().shr(4) and 0xf]
        result[index++] = HEX_DIGITS[b.toInt() and 0xf]
    }
    return String(result)
}

/**
 * hex string to byte array
 */
fun String.hex2Bytes(): ByteArray {
    var hexString = if (this.startsWith("0x") || this.startsWith("0X")) {
        this.substring(2, this.length)
    } else {
        this
    }
    var len = hexString.length
    if (len % 2 != 0) {
        hexString = "0$hexString"
        len++
    }
    val hexBytes = hexString.toUpperCase(Locale.getDefault()).toCharArray()
    val ret = ByteArray(len shr 1)
    var i = 0
    while (i < len) {
        ret[i shr 1] = ((hexBytes[i].hexToInt()) shl 4 or (hexBytes[i + 1].hexToInt())).toByte()
        i += 2
    }
    return ret
}

fun Char.hexToInt(): Int {
    return when (this) {
        in '0'..'9' -> this - '0'
        in 'A'..'F' -> this - 'A' + 10
        else -> throw IllegalArgumentException()
    }
}

/**
 * byte array to int
 */
fun ByteArray.toInt(): Int = TransformUtils.bytes2Int(this)

/**
 * int to byte array
 */
fun Int.toByteArray(): ByteArray = TransformUtils.int2Bytes(this)

/**
 * byte array to short
 */
fun ByteArray.toShort(): Short = TransformUtils.bytes2Short(this)

/**
 * short to byte array
 */
fun Short.toByteArray(): ByteArray = TransformUtils.short2Bytes(this)

/**
 * byte array to long
 */
fun ByteArray.toLong(): Long = TransformUtils.bytes2Long(this)

/**
 * long to byte array
 */
fun Long.toByteArray(): ByteArray = TransformUtils.long2Bytes(this)

/**
 * bitmap to byte array
 */
fun Bitmap.toByteArray(format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): ByteArray {
    ByteArrayOutputStream().use {
        compress(format, 100, it)
        return it.toByteArray()
    }
}

/**
 * byte array to bitmap
 */
fun ByteArray.toBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, size)

/**
 * drawable to bitmap
 */
fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) return bitmap

    val bitmap = if (intrinsicHeight <= 0 || intrinsicWidth <= 0) {
        Bitmap.createBitmap(
            1,
            1,
            if (opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        )
    } else {
        Bitmap.createBitmap(
            intrinsicWidth,
            intrinsicHeight,
            if (opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        )
    }

    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

/**
 * bitmap to drawable
 */
fun Bitmap.toDrawable(context: Context): Drawable = BitmapDrawable(context.resources, this)

/**
 * drawable to byte array
 */
fun Drawable.toByteArray(format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): ByteArray =
    toBitmap().toByteArray(format)

/**
 * byte array to drawable
 */
fun ByteArray.toDrawable(context: Context): Drawable = toBitmap().toDrawable(context)
