package com.example.contact

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_splash_screen.*
import kotlinx.coroutines.delay
import java.util.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        var topAnimation = AnimationUtils.loadAnimation(this,R.anim.topanimation)
        var bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottomanimation)
        splashImage.animation = topAnimation
        splashAppname.animation = bottomAnimation
        var intent = Intent(this,UserActivity::class.java)
        Timer().schedule(object : TimerTask(){
            override fun run() {
                startActivity(intent)
                finish()
            }
        },3000)
    }
}