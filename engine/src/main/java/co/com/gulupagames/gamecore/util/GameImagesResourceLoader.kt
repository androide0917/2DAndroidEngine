package co.com.gulupagames.gamecore.util

import android.content.AsyncTaskLoader
import co.com.gulupagames.gamecore.game.gameactivity.BaseGameActivity

class GameImagesResourceLoader(private val context: BaseGameActivity) : AsyncTaskLoader<Any?>(
    context
) {
    override fun loadInBackground(): Any? {
        context.loadGameImagesResources()
        return null
    }
}