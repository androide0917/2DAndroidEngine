package co.com.gulupagames.gamecore.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.Log
import android.util.SparseArray
import androidx.core.content.ContextCompat

class SpriteCache(private val context: Context) {
    private val resources = SparseArray<Bitmap?>()
    private val resourcesMaxSize = SparseArray<Bitmap?>()
    private var spriteCanvas: Bitmap? = null
    fun getSprite(drawable: Int, newWidth: Float, newHeight: Float): Bitmap? {
        var image = resources[drawable]
        if (image == null) {
            try {
                image = getBitmapFromVectorDrawable(context, drawable)
            } catch (e: Exception) {
                Log.e("ERROR", "Error trying to load image: " + e.message)
                System.gc()
            }
            if (image != null) {
                val width = image.width
                val height = image.height
                val scaleWidth = newWidth / width
                val scaleHeight = newHeight / height
                val matrix = Matrix()
                matrix.postScale(scaleWidth, scaleHeight)
                image = Bitmap.createBitmap(image, 0, 0, width, height, matrix, false)
                resources.put(drawable, image)
            }
        }
        return image
    }

    fun getCachedSprite(drawable: Int): Bitmap? {
        return resources[drawable]
    }

    fun getSpriteMaxSize(drawable: Int, newWidth: Int, newHeight: Int): Bitmap? {
        var image = resourcesMaxSize[drawable]
        if (image == null) {
            image = getSprite(drawable, newWidth.toFloat(), newHeight.toFloat())
            resourcesMaxSize.put(drawable, image)
        }
        return image
    }

    fun getSpriteCanvas(myCanvasWidth: Int, myCanvasHeight: Int): Bitmap? {
        if (spriteCanvas == null) {
            spriteCanvas =
                Bitmap.createBitmap(myCanvasWidth, myCanvasHeight, Bitmap.Config.ARGB_8888)
        }
        return spriteCanvas
    }

    companion object {
        private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap? {
            val drawable = ContextCompat.getDrawable(context, drawableId)
            var bitmap: Bitmap? = null
            if (drawable != null) {
                bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
            }
            return bitmap
        }
    }
}