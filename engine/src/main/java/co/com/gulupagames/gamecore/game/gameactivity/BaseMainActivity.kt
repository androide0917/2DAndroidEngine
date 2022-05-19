package co.com.gulupagames.gamecore.game.gameactivity

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import co.com.gulupagames.gamecore.R

abstract class BaseMainActivity : Activity() {
    private val startGameHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            gameBegin()
        }
    }
    @JvmField
    protected var gameFactoryIcon: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    private fun startGame() {
        Thread {
            try {
                Thread.sleep(WAIT_FOR_LOGO)
                startGameHandler.sendMessage(Message())
            } catch (e: InterruptedException) {
                Log.e("ERROR", "Error trying to wait for logo", e)
            }
        }.start()
    }

    private fun animateLogo() {
        val animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 2000
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                val animation1 = AnimationUtils.loadAnimation(this@BaseMainActivity, R.anim.shake)
                animation1.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {
                        val animation2 = AnimationUtils.loadAnimation(
                            this@BaseMainActivity,
                            R.anim.slide_out_left
                        )
                        animation2.setAnimationListener(object : AnimationListener {
                            override fun onAnimationStart(animation: Animation) {}
                            override fun onAnimationEnd(animation: Animation) {
                                gameFactoryIcon!!.visibility = View.GONE
                            }

                            override fun onAnimationRepeat(animation: Animation) {}
                        })
                        gameFactoryIcon!!.startAnimation(animation2)
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                gameFactoryIcon!!.startAnimation(animation1)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        gameFactoryIcon!!.startAnimation(animation)
    }

    protected fun animateExit() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        finish()
    }

    override fun onResume() {
        super.onResume()
        startGame()
        animateLogo()
    }

    protected abstract fun gameBegin()

    companion object {
        private const val WAIT_FOR_LOGO: Long = 3600
    }
}