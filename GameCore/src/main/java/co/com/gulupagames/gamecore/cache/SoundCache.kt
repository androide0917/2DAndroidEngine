package co.com.gulupagames.gamecore.cache

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.util.SparseArray
import co.com.gulupagames.gamecore.game.gameactivity.BaseGameActivity

class SoundCache(baseGameActivity: BaseGameActivity) {
    private val soundPoolMap: SparseArray<Int>
    private val soundPool: SoundPool
    private var loopSound: MediaPlayer? = null
    private val baseGameActivity: BaseGameActivity
    private fun createSoundPool(): SoundPool {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        return SoundPool.Builder().setMaxStreams(MAX_STREAMS)
            .setAudioAttributes(audioAttributes).build()
    }

    fun playSound(soundID: Int) {
        val mp = soundPoolMap[soundID]
        if (mp == null) {
            soundPoolMap.put(soundID, soundPool.load(baseGameActivity.baseGameActivity, soundID, 1))
            soundPool.play(soundPoolMap[soundID], 1f, 1f, 1, 0, 1f)
        } else {
            soundPool.play(soundPoolMap[soundID], 1f, 1f, 1, 0, 1f)
        }
    }

    fun startLoopSound() {
        if (loopSound != null) {
            loopSound?.start()
        } else {
            loopSound = null
            loopSound = baseGameActivity.getLoopSound()
            loopSound?.isLooping = true
            loopSound?.start()
        }
    }

    fun pauseLoopSound() {
        if (loopSound != null) {
            loopSound?.pause()
        }
    }

    fun stopLoopSound() {
        if (loopSound != null) {
            loopSound?.pause()
            loopSound = null
        }
    }

    companion object {
        private const val MAX_STREAMS = 30
    }

    init {
        soundPool = createSoundPool()
        this.baseGameActivity = baseGameActivity
        soundPoolMap = SparseArray()
    }
}