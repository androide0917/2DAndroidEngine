package co.com.gulupagames.spacecraft.levelengine

import android.util.SparseArray
import co.com.gulupagames.gamecore.entity.GameEntity
import co.com.gulupagames.gamecore.game.gameactivity.BaseGameActivity
import co.com.gulupagames.gamecore.game.surfaceview.helpers.LevelEngineCore
import co.com.gulupagames.gamecore.game.surfaceview.updateworldengine.ILevelEngine
import co.com.gulupagames.gamecore.util.LineCounter
import co.com.gulupagames.spacecraft.R
import co.com.gulupagames.spacecraft.entity.ConstantEntity
import co.com.gulupagames.spacecraft.entity.entity.*
import java.util.concurrent.CopyOnWriteArrayList

class LevelEngine(private val baseGameActivity: BaseGameActivity) : ILevelEngine {
    override fun loadData(
        line: String?,
        levelEngineCore: LevelEngineCore?
    ): MutableList<GameEntity> {
        val response: MutableList<GameEntity> = CopyOnWriteArrayList()
        val data = arrayOfNulls<String>(7)
        data[0] = validateData(line!!.substring(0, 1))
        data[1] = validateData(line.substring(1, 2))
        data[2] = validateData(line.substring(2, 3))
        data[3] = validateData(line.substring(3, 4))
        data[4] = validateData(line.substring(4, 5))
        data[5] = validateData(line.substring(5, 6))
        data[6] = validateData(line.substring(6, 7))
        for (i in data.indices) {
            val entity = data[i]
            if (entity != "0") {
                when (entity) {
                    "1" -> {
                        val player = GamePlayer(levelEngineCore!!.levelSurfaceView)
                        player.x = (levelEngineCore.levelSurfaceView.myCanvasWFixed / 7 * i)
                        player.y = (levelEngineCore.levelSurfaceView.myCanvasHFixed - player.height)
                        levelEngineCore.levelSurfaceView.iGamePlayer = player
                        player.shields = (GamePlayer.MAX_SHIELDS)
                    }
                    "2" -> {
                        val alien = Alien1(levelEngineCore!!.levelSurfaceView)
                        alien.x = (levelEngineCore.levelSurfaceView.myCanvasWFixed / 7 * i)
                        alien.y = (0)
                        alien.initVx(3)
                        response.add(alien)
                    }
                    "3" -> {
                        val alien2 = Alien2(levelEngineCore!!.levelSurfaceView)
                        alien2.x = (levelEngineCore.levelSurfaceView.myCanvasWFixed / 7 * i)
                        alien2.y = (0)
                        alien2.initVx(3)
                        response.add(alien2)
                    }
                    "4" -> {
                        val asteroid = Asteroid(levelEngineCore!!.levelSurfaceView)
                        asteroid.x = (levelEngineCore.levelSurfaceView.myCanvasWFixed / 7 * i)
                        asteroid.y = (0)
                        asteroid.initVx(5)
                        response.add(asteroid)
                    }
                    "5" -> {
                        val alien3 = Alien3(levelEngineCore!!.levelSurfaceView)
                        alien3.x = (levelEngineCore.levelSurfaceView.myCanvasWFixed / 7 * i)
                        alien3.y = (0)
                        alien3.initVx(5)
                        response.add(alien3)
                    }
                    "6" -> {
                        val alien4 = Alien4(levelEngineCore!!.levelSurfaceView)
                        alien4.x = (levelEngineCore.levelSurfaceView.myCanvasWFixed / 7 * i)
                        alien4.y = (0)
                        alien4.initVx(8)
                        response.add(alien4)
                    }
                    "7" -> {
                        val addShields = AddShields(levelEngineCore!!.levelSurfaceView)
                        addShields.x = (levelEngineCore.levelSurfaceView.myCanvasWFixed / 7 * i)
                        addShields.y = (0)
                        response.add(addShields)
                    }
                    "8" -> {
                        val addBombs = AddBombs(levelEngineCore!!.levelSurfaceView)
                        addBombs.x = (levelEngineCore.levelSurfaceView.myCanvasWFixed / 7 * i)
                        addBombs.y = (0)
                        response.add(addBombs)
                    }
                }
            }
        }
        return response
    }

    private fun validateData(data: String): String {
        var find = false
        for (cName in ConstantEntity.values()) {
            if (data == cName.value.toString() + "") {
                find = true
            }
        }
        return if (!find) {
            "0"
        } else {
            data
        }
    }

    override fun initLevels(levelEngineCore: LevelEngineCore?) {
        val levels = SparseArray<LineCounter>()
        var count = 0
        var `is` = baseGameActivity.resources.openRawResource(R.raw.level1)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level2)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level3)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level4)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level5)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level6)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level7)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level8)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level9)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level10)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level11)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level12)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level13)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level14)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level15)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level16)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level17)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level18)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level19)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level20)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level21)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level22)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level23)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level24)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level25)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level26)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level27)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level28)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level29)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level30)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level31)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level32)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level33)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level34)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level35)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level36)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level37)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level38)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level39)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level40)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level41)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level42)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level43)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level44)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level45)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level46)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level47)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level48)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level49)
        levels.put(++count, LineCounter(`is`))
        `is` = baseGameActivity.resources.openRawResource(R.raw.level50)
        levels.put(++count, LineCounter(`is`))
        val maxLevel = count
        levelEngineCore?.setLevels(levels)
        levelEngineCore?.maxLevel = maxLevel
    }
}