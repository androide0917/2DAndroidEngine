package co.com.gulupagames.gamecore.util

import android.util.Log
import java.io.IOException
import java.io.InputStream
import kotlin.Throws

class LineCounter(`is`: InputStream) {
    var maxLines = 0.0
    var inputStream: InputStream? = null
    @Throws(IOException::class)
    fun count(`is`: InputStream): Int {
        return try {
            val c = ByteArray(1024)
            var count = 0
            var readChars: Int
            var endsWithoutNewLine = false
            while (`is`.read(c).also { readChars = it } != -1) {
                for (i in 0 until readChars) {
                    if (c[i].toChar() == '\n') ++count
                }
                endsWithoutNewLine = c[readChars - 1].toChar() != '\n'
            }
            if (endsWithoutNewLine) {
                ++count
            }
            count
        } finally {
            `is`.reset()
        }
    }

    init {
        try {
            inputStream = `is`
            maxLines = count(`is`).toDouble()
        } catch (e: IOException) {
            Log.e("ERROR", "Error counting level lines")
        }
    }
}