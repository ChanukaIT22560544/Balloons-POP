package com.example.balloonspop
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class HighScoreActivity : AppCompatActivity() {

    private lateinit var highScoreDisplay: TextView
    private lateinit var resetButton: Button

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)

        highScoreDisplay = findViewById(R.id.highScoreText)
        resetButton = findViewById(R.id.resetButton)

        sharedPreferences = getSharedPreferences("TapToPop", MODE_PRIVATE)

        val highScore = sharedPreferences.getInt("HIGH_SCORE", 0)
        highScoreDisplay.text = "High Score: $highScore"

        resetButton.setOnClickListener {
            resetHighScore()
        }
    }

    private fun resetHighScore() {
        sharedPreferences.edit().remove("HIGH_SCORE").apply()
        highScoreDisplay.text = "High Score: 0"
    }


}
