package com.example.contact.activity

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.contact.R
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.util.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val topAnimation = AnimationUtils.loadAnimation(this, R.anim.topanimation)
        val bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottomanimation)
        splashImage.animation = topAnimation
        splashAppname.animation = bottomAnimation
        val intent = Intent(this, UserActivity::class.java)
        Timer().schedule(object : TimerTask(){
            override fun run() {
                startActivity(intent)
                finish()
            }
        },3000)
    }
}