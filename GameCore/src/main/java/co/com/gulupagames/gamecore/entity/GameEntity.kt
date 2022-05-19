package co.com.gulupagames.gamecore.entity

import android.graphics.Canvas
import android.util.Log
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.model.Rectangle

open class GameEntity(protected val levelSurfaceView: LevelSurfaceView) {
    private lateinit var spriteNames: IntArray
    private lateinit var rightSprites: IntArray
    private var leftSprites: IntArray? = null
    var x = 0
    var y = 0
    protected var vx = 0
    protected var vy = 0
    var width = 0
    var height = 0
    protected var currentFrame = 0
    protected var frameSpeed = 0
    private var actualTime = 0
    var isMarkedForRemoval = false
        protected set
    val bounds: Rectangle
        get() = Rectangle(x, y, width, height)

    open fun paintCanvasView(canvas: Canvas) {
        canvas.drawBitmap(
            levelSurfaceView.baseGameActivity!!.spriteCache!!.getSprite(
                spriteNames[currentFrame],
                width.toFloat(),
                height.toFloat()
            )!!, x.toFloat(), y.toFloat(), null
        )
    }

    fun setSpriteNames(names: IntArray) {
        spriteNames = names
        setSize()
    }

    fun setRightSprites(rightSprites: IntArray) {
        this.rightSprites = rightSprites
        setSize()
    }

    protected fun setSize() {
        try {
            width =
                levelSurfaceView.baseGameActivity!!.spriteCache!!.getCachedSprite(rightSprites[0])!!
                    .width
            height =
                levelSurfaceView.baseGameActivity!!.spriteCache!!.getCachedSprite(rightSprites[0])!!
                    .height
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d("Error", it) }
        }
    }

    open fun act() {
        updateFrame()
        frameDirectionAdjust()
    }

    protected fun updateFrame() {
        if (frameSpeed != 0) {
            actualTime++
            if (actualTime % frameSpeed == 0) {
                actualTime = 0
                currentFrame = (currentFrame + 1) % spriteNames.size
            }
        }
        if (y > levelSurfaceView.myCanvasHFixed) {
            remove()
        }
    }

    protected fun frameDirectionAdjust() {
        if (vx >= 0 || leftSprites == null) {
            setSpriteNames(rightSprites)
        } else if (vx < 0) {
            setSpriteNames(leftSprites!!)
        }
    }

    fun remove() {
        isMarkedForRemoval = true
    }

    open fun collision(entity: GameEntity) {}
    fun touch() {}
    fun setLeftSprites(leftSprites: IntArray?) {
        this.leftSprites = leftSprites
    }

    fun getSpriteNames(): IntArray {
        return spriteNames
    }
}