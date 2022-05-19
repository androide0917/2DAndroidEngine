package co.com.gulupagames.spacecraft.activities.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import co.com.gulupagames.gamecore.game.gameactivity.BaseMainActivity
import co.com.gulupagames.spacecraft.R
import co.com.gulupagames.spacecraft.activities.game.GameActivity

class MainActivity : BaseMainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_view)
        gameFactoryIcon = findViewById<View>(R.id.gulupa_games) as ImageView
    }

    override fun gameBegin() {
        startActivity(Intent(applicationContext, GameActivity::class.java))
        animateExit()
    }
}