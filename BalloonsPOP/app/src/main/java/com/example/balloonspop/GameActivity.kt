package com.example.balloonspop

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity



class GameActivity : AppCompatActivity() {

    private lateinit var balloon: ImageView
    private lateinit var scoreDisplay: TextView
    private lateinit var lifeDisplay: TextView
    private lateinit var timerDisplay: TextView
    private lateinit var popSound: MediaPlayer
    private lateinit var gameOverSound: MediaPlayer
    private lateinit var backgroundSound: MediaPlayer

    private var score = 0
    private var lives = 3
    private var balloonSpeed = 3000L  // Initial balloon speed

    private var gameTimer: CountDownTimer? = null
    private var gameDuration = 60000L  // Game duration (e.g., 60 seconds)

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        balloon = findViewById(R.id.balloonContainer)
        scoreDisplay = findViewById(R.id.scoreText)
        lifeDisplay = findViewById(R.id.livesText)
        timerDisplay = findViewById(R.id.timerText)

        popSound = MediaPlayer.create(this, R.raw.tap_sound)
        gameOverSound = MediaPlayer.create(this, R.raw.game_over_sound)
        sharedPreferences = getSharedPreferences("TapToPop", MODE_PRIVATE)

        balloon.setOnClickListener {
            popBalloon()
        }


        backgroundSound = MediaPlayer.create(this, R.raw.background)
        backgroundSound.isLooping = true  // Loop the sound

        startGame()
    }

    private fun startGame() {
        score = 0
        lives = 3
        updateUI()
        // Start background sound
        backgroundSound.start()
        startCountdownTimer()
        dropBalloon()  // Start the first balloon drop
    }

    private fun scheduleNextDrop() {
        balloon.postDelayed({
            dropBalloon()
        }, 10)
    }

    private fun dropBalloon() {
        // Reset balloon's position to bottom
        balloon.translationY = (balloon.parent as View).height.toFloat()


        val randomX = (0..(balloon.parent as View).width - balloon.width).random().toFloat()
        balloon.x = randomX


        val upperBound = (balloon.parent as View).height.toFloat() * 0.7f

        // Start the animation
        balloon.animate()
            .translationY(-upperBound)  // Adjust upper bound as needed
            .setDuration(balloonSpeed)
            .withEndAction {
                if (lives > 0) {
                    lives--
                    updateUI()
                    if (lives > 0) {
                        scheduleNextDrop()
                    } else {
                        endGame()
                    }
                }
            }.start()

        // Speed up next drop
        if (balloonSpeed > 500) balloonSpeed -= 100
    }


    private fun popBalloon() {

        score++
        updateUI()
        popSound.start()
        balloon.clearAnimation()
        scheduleNextDrop()  //schedule  next balloon to drop
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        scoreDisplay.text = "Score: $score"
        lifeDisplay.text = "Lives: $lives"
    }

    private fun endGame() {
        gameTimer?.cancel()
        gameOverSound.start()
        backgroundSound.stop()  // Stop  background sound
        saveHighScore()
        val currentScoreMessage = "Score:$score"
        showToast("Game Over!")
        showToast(currentScoreMessage)

        val intent = Intent(this, GameOverActivity::class.java)
        intent.putExtra("SCORE", score)
        startActivity(intent)
        finish()
    }

    private fun saveHighScore() {
        val highScore = sharedPreferences.getInt("HIGH_SCORE", 0)
        if (score > highScore) {
            sharedPreferences.edit().putInt("HIGH_SCORE", score).apply()
        }
    }

    private fun startCountdownTimer() {
        gameTimer = object : CountDownTimer(gameDuration, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timerDisplay.text = "Time: ${secondsLeft}s"
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                timerDisplay.text = "Time: 0s"
                endGame()
            }
        }.start()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        popSound.release()
        gameOverSound.release()
    }
}
