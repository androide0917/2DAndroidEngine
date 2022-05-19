package co.com.gulupagames.spacecraft.entity.entity

import android.graphics.Color
import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.entity.IGamePlayer
import co.com.gulupagames.gamecore.game.surfaceview.LevelSurfaceView
import co.com.gulupagames.gamecore.util.Util.horizontalDistanceAdjust
import co.com.gulupagames.spacecraft.R

class GamePlayer(levelSurfaceView: LevelSurfaceView) : GameEntity(levelSurfaceView), IGamePlayer {

    override var shields = 0

    private var shootsInStage = 0
    private val basePlayerSpeed: Int

    var shield: Shield
    var up = false
    var down = false
    var left = false
    var right = false
    var death = false
    var laserShoots = 0
    var bombShoots = 0
    var playerSpeed: Int

    override fun setShootsInStage(shoots: Int) {
        this.shootsInStage = shoots
    }

    override fun act() {
        if (!death) {
            updateFrame()
            x += vx
            y += vy
            if (x < 0)
                x = 0
            if (x > levelSurfaceView.myCanvasWFixed - width)
                x = levelSurfaceView.myCanvasWFixed - width
            if (y < -height)
                y = 0
            if (y > levelSurfaceView.myCanvasWFixed - height)
                y = levelSurfaceView.myCanvasWFixed - height
            frameDirectionAdjust()
        }
    }

    private fun updateSpeed() {
        vx = 0
        vy = 0
        if (down) vy = playerSpeed
        if (up) vy = -playerSpeed
        if (left) vx = -playerSpeed
        if (right) vx = playerSpeed
    }

    override fun keyReleased(action: Int) {
        if (!death) {
            when (action) {
                LevelSurfaceView.LEFT -> left = false
                LevelSurfaceView.RIGHT -> right = false
                LevelSurfaceView.UP -> up = false
                LevelSurfaceView.DOWN -> down = false
                LevelSurfaceView.LASER -> fire()
                LevelSurfaceView.BOMB -> fireCluster()
            }
            updateSpeed()
        }
    }

    override fun keyPressed(action: Int) {
        if (!death) {
            when (action) {
                LevelSurfaceView.UP -> up = true
                LevelSurfaceView.DOWN -> down = true
            }
            updateSpeed()
        }
    }

    override fun keyPressed(action: Int, curDir: Float) {
        if (!death) {
            when (action) {
                LevelSurfaceView.LEFT -> {
                    left = true
                    evaluateCurDirLeft(curDir)
                }
                LevelSurfaceView.RIGHT -> {
                    right = true
                    evaluateCurDirRight(curDir)
                }
            }
            updateSpeed()
        }
    }

    private fun evaluateCurDirLeft(curDir: Float) {
        if (curDir <= -0.5 && curDir > -1) {
            playerSpeed = basePlayerSpeed + 3
        } else if (curDir <= -1 && curDir > -3) {
            playerSpeed = basePlayerSpeed + 4
        } else if (curDir <= -3 && curDir > -5) {
            playerSpeed = basePlayerSpeed + 5
        } else if (curDir <= -5 && curDir > -7) {
            playerSpeed = basePlayerSpeed + 6
        } else if (curDir <= -7 && curDir > -10) {
            playerSpeed = basePlayerSpeed + 7
        }
    }

    private fun evaluateCurDirRight(curDir: Float) {
        if (curDir >= 0.5 && curDir < 1) {
            playerSpeed = basePlayerSpeed + 3
        } else if (curDir >= 1 && curDir < 3) {
            playerSpeed = basePlayerSpeed + 4
        } else if (curDir >= 3 && curDir < 5) {
            playerSpeed = basePlayerSpeed + 5
        } else if (curDir >= 5 && curDir < 7) {
            playerSpeed = basePlayerSpeed + 6
        } else if (curDir >= 7 && curDir < 10) {
            playerSpeed = basePlayerSpeed + 7
        }
    }

    private fun fire() {
        if (shootsInStage < MAX_SHOOT_IN_STAGE && !death) {
            val b = Laser(levelSurfaceView)
            b.x = x + width / 2
            b.y = y - b.height
            levelSurfaceView.addActor(b)
            levelSurfaceView.baseGameActivity!!.soundCache!!.playSound(R.raw.laser)
            shootsInStage++
            laserShoots++
        }
    }

    private fun fireCluster() {
        if (!death) {
            if (levelSurfaceView.clusterBombs == 0) {
                return
            }
            levelSurfaceView.addClusterBombs(-1)
            bombShoots++
            levelSurfaceView.baseGameActivity?.soundCache?.playSound(R.raw.bomb)
            levelSurfaceView.addActor(Bomb(levelSurfaceView, Bomb.UP_LEFT, x, y))
            levelSurfaceView.addActor(Bomb(levelSurfaceView, Bomb.UP, x, y))
            levelSurfaceView.addActor(Bomb(levelSurfaceView, Bomb.UP_RIGHT, x, y))
            levelSurfaceView.addActor(Bomb(levelSurfaceView, Bomb.LEFT, x, y))
            levelSurfaceView.addActor(Bomb(levelSurfaceView, Bomb.RIGHT, x, y))
            //levelSurfaceView.addActor(new Bomb(levelSurfaceView, Bomb.DOWN_LEFT, x, y));
            //levelSurfaceView.addActor(new Bomb(levelSurfaceView, Bomb.DOWN, x, y));
            //levelSurfaceView.addActor(new Bomb(levelSurfaceView, Bomb.DOWN_RIGHT, x, y));
        }
    }

    override fun addScore(plusScore: Int, entity: GameEntity) {
        if (!death) {
            val m = Status(
                levelSurfaceView,
                entity,
                plusScore.toString() + levelSurfaceView.resources.getString(R.string.score_s),
                Color.GREEN
            )
            levelSurfaceView.addActor(m)
            levelSurfaceView.addScore(plusScore)
        }
    }

    override fun addShields(shields: Int) {
        if (!death) {
            if (this.shields <= 100 && this.shields + shields > 100) {
                shield = Shield(levelSurfaceView)
                shield.x = x
                shield.y = y
                levelSurfaceView.actors?.plus(shield)
            }
            if (shields > 0) {
                val m = Status(
                    levelSurfaceView,
                    levelSurfaceView.iGamePlayer as GamePlayer?,
                    shields.toString() + levelSurfaceView.resources.getString(R.string.shields_s),
                    Color.GREEN
                )
                levelSurfaceView.addActor(m)
            } else {
                val m = Status(
                    levelSurfaceView,
                    levelSurfaceView.iGamePlayer as GamePlayer?,
                    shields.toString() + levelSurfaceView.resources.getString(R.string.shields_s),
                    Color.RED
                )
                levelSurfaceView.addActor(m)
            }
            this.shields += shields
            if (this.shields > MAX_SHIELDS) {
                this.shields = MAX_SHIELDS
            }
        }
    }

    override fun addBombs(bombs: Int) {
        if (!death) {
            val m = Status(
                levelSurfaceView,
                levelSurfaceView.iGamePlayer as GamePlayer?,
                bombs.toString() + levelSurfaceView.resources.getString(R.string.bombs_s),
                Color.GREEN
            )
            levelSurfaceView.addActor(m)
            levelSurfaceView.addClusterBombs(bombs)
        }
    }

    override var clusterBombs: Int
        get() = levelSurfaceView.clusterBombs
        set(bombs) {
            levelSurfaceView.clusterBombs = bombs
        }

    override fun collision(entity: GameEntity) {
        if (!death) {
            when (entity) {
                is Alien1 -> {
                    entity.remove()
                    addScore(100, entity)
                    addShields(-50)
                }
                is Alien2 -> {
                    entity.remove()
                    addScore(100, entity)
                    addShields(-50)
                }
                is Alien3 -> {
                    entity.remove()
                    addScore(100, entity)
                    addShields(-100)
                }
                is Asteroid -> {
                    entity.remove()
                    addScore(100, entity)
                    addShields(-100)
                }
                is AlienShoot -> {
                    entity.remove()
                    addShields(-25)
                }
                is AlienShoot2 -> {
                    entity.remove()
                    addShields(-50)
                }
            }
            if (shields <= 0 && !death) {
                setSpriteNames(intArrayOf(R.drawable.invisible))
                val m = Death(levelSurfaceView)
                m.x = x
                m.y = y
                y = height + levelSurfaceView.myCanvasHFixed
                death = true
                levelSurfaceView.addActor(m)
            }
        }
    }

    override fun isDeath(): Boolean {
        if (death) releaseAll()
        return death
    }

    private fun releaseAll() {
        left = false
        right = false
        up = false
        down = false
    }

    companion object {
        const val MAX_SHIELDS = 500
        private const val MAX_SHOOT_IN_STAGE = 15
    }

    init {
        setRightSprites(intArrayOf(R.drawable.navesmall))
        setSize()
        basePlayerSpeed = horizontalDistanceAdjust(10, levelSurfaceView.myCanvasWFixed)
        playerSpeed = horizontalDistanceAdjust(10, levelSurfaceView.myCanvasWFixed)
        shield = Shield(levelSurfaceView)
        shield.x = x
        shield.y = y
        levelSurfaceView.addActor(shield)
        frameDirectionAdjust()
    }
}