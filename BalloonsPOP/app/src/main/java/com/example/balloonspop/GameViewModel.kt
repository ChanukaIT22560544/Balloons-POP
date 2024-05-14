package com.example.balloonspop

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    var score: Int = 0
    var lives: Int = 3

    fun incrementScore() {
        score++
    }

    fun decrementLives() {
        lives--
    }
}
