package co.com.gulupagames.gamecore.game.surfaceview.updateworldengine

import co.com.gulupagames.gamecore.game.surfaceview.helpers.LevelEngineCore
import co.com.gulupagames.gamecore.entity.GameEntity

interface ILevelEngine {
    fun loadData(line: String?, levelEngineCore: LevelEngineCore?): MutableList<GameEntity>?
    fun initLevels(levelEngineCore: LevelEngineCore?)
}