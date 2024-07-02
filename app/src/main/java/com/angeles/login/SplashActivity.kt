package com.angeles.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.logging.Handler

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 3000 // 3 segundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val imageView: ImageView = findViewById(R.id.logoImageView)

        // Animación fade_in (si tienes el archivo fade_in.xml en res/anim)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.anim)
        imageView.startAnimation(fadeInAnimation)

        // Animación de escala con ObjectAnimator
        val scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.2f)
        val scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.2f)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY)
        animatorSet.duration = 2000 // 1 segundo
        animatorSet.start()

        // Retraso para iniciar LoginActivity
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}