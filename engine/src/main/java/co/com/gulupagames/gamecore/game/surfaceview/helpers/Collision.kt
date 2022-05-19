package co.com.gulupagames.gamecore.game.surfaceview.helpers

import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.model.Rectangle

class Collision(private val levelSurfaceView: LevelSurfaceView) {
    fun checkCollisions() {
        levelSurfaceView.actors?.let {
            if (levelSurfaceView.iGamePlayer != null) {
                val playerRectangle = levelSurfaceView.iGamePlayer?.bounds
                for (counter in it.indices) {
                    val entity = it[counter]
                    val entityRectangle = entity.bounds
                    checkCollisionPlayer(entity, entityRectangle, playerRectangle)
                }
            }
            for (counter in it.indices) {
                val entity = it[counter]
                val entityRectangle = entity.bounds
                checkCollisionEntity(entity, entityRectangle, counter)
            }
            remove()
        }
    }

    private fun checkCollisionEntity(entity: GameEntity, entityRectangle: Rectangle, counter: Int) {
        for (j in counter + 1 until levelSurfaceView.actors!!.size) {
            val tmpEntity = levelSurfaceView.actors!![j]
            val tmpRectangle = tmpEntity.bounds
            if (Rectangle.checkCollision(tmpRectangle, entityRectangle)) {
                entity.collision(tmpEntity)
                tmpEntity.collision(entity)
            }
        }
    }

    private fun checkCollisionPlayer(
        entity: GameEntity,
        entityRectangle: Rectangle,
        playerRectangle: Rectangle?
    ) {
        playerRectangle?.let {
            if (Rectangle.checkCollision(it, entityRectangle)) {
                levelSurfaceView.iGamePlayer?.collision(entity)
                entity.collision(levelSurfaceView.iGamePlayer as GameEntity)
            }
        }
    }

    private fun remove() {
        levelSurfaceView.actors?.let {
            var i = 0
            while (i < it.size) {
                val entity = it[i]
                if (entity.isMarkedForRemoval) {
                   levelSurfaceView.actors?.removeAt(i)
                }
                i++
            }
        }
    }

    fun checkTouchCollisions(touchRectangle: Rectangle?) {
        for (i in levelSurfaceView.actors!!.indices) {
            val entity = levelSurfaceView.actors!![i]
            val tmpRectangle = entity.bounds
            touchRectangle?.let {
                if (Rectangle.checkCollision(tmpRectangle, touchRectangle)) {
                    entity.touch()
                }
            }
        }
    }
}