package com.example.balloonspop

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOverActivity : AppCompatActivity() {

    private lateinit var scoreDisplayTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        scoreDisplayTextView = findViewById(R.id.scoreDisplayTextView)
        sharedPreferences = getSharedPreferences("TapToPop", MODE_PRIVATE)

        val score = intent.getIntExtra("SCORE", 0)
        scoreDisplayTextView.text = " $score"
    }

    fun restartGame(view: android.view.View) {
        startActivity(Intent(this, GameActivity::class.java))
        finish()
    }

    fun exitGame(view: android.view.View) {
        finishAffinity()
    }

    fun navigateToMainActivity(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
