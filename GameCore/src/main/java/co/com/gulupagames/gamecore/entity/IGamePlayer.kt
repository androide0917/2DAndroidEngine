package co.com.gulupagames.gamecore.entity

import android.graphics.Canvas
import co.com.gulupagames.gamecore.model.Rectangle

interface IGamePlayer {
    val x: Int
    val y: Int
    val width: Int
    var clusterBombs: Int
    var shields: Int
    val bounds: Rectangle?
    fun collision(entity: GameEntity)
    fun isDeath(): Boolean
    fun keyPressed(action: Int)
    fun keyPressed(action: Int, curDir: Float)
    fun keyReleased(action: Int)
    fun paintCanvasView(canvas: Canvas)
    fun act()
    fun addBombs(bombs: Int)
    fun addShields(shields: Int)
    fun setShootsInStage(shoots: Int)
    fun addScore(plusScore: Int, entity: GameEntity)
}
