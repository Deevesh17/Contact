package com.example.contact

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.util.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash_screen)
        var topAnimation = AnimationUtils.loadAnimation(this,R.anim.topanimation)
        var bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottomanimation)
        splashImage.animation = topAnimation
        splashAppname.animation = bottomAnimation
        var intent = Intent(this,UserActivity::class.java)
        Handler().postDelayed(object : Runnable{
            override fun run() {
                startActivity(intent)
                finish()
            }
        },3000)

    }
}