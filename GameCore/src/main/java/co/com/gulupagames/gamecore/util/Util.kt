package co.com.gulupagames.gamecore.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.view.View
import co.com.gulupagames.gamecore.cache.SpriteCache
import java.text.DecimalFormat
import java.text.NumberFormat

object Util {
    private const val FONT_COLOR = Color.WHITE
    private const val FONT_SIZE = 25
    private const val BASE_WIDTH = 1280
    private const val BASE_HEIGHT = 720
    private val formatter: NumberFormat = DecimalFormat("#0.0")
    @JvmStatic
    @JvmOverloads
    fun drawString(
        canvas: Canvas,
        message: String?,
        x: Int,
        y: Int,
        color: Int = FONT_COLOR,
        size: Int = FONT_SIZE
    ) {
        val p = Paint()
        p.color = color
        p.textSize = size.toFloat()
        canvas.drawText(message!!, x.toFloat(), y.toFloat(), p)
    }

    fun formatDouble(number: Double): String {
        return formatter.format(number)
    }

    @JvmStatic
    fun horizontalDistanceAdjust(baseDistance: Int, width: Int): Int {
        return width * baseDistance / BASE_WIDTH
    }

    @JvmStatic
    fun verticalDistanceAdjust(baseDistance: Int, height: Int): Int {
        return height * baseDistance / BASE_HEIGHT
    }

    fun isTablet(context: Context): Boolean {
        val xlarge =
            context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == 4
        val large =
            context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_LARGE
        return xlarge || large
    }

    @JvmStatic
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (null != activeNetwork) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) return true
            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) return true
        }
        return false
    }

    fun setBackGroundSvg(
        view: View,
        background_id: Int,
        resources: Resources?,
        spriteCache: SpriteCache,
        width: Int,
        height: Int
    ) {
        view.background = BitmapDrawable(
            resources,
            spriteCache.getSprite(background_id, width.toFloat(), height.toFloat())
        )
    }
}