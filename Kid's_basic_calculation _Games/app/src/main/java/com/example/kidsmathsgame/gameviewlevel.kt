// GameViewModel.kt - menangani logika permainan dengan LiveData
package com.example.kidsmathgame

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private var correctAnswer = 0
    private var totalQuestions = 0
    private var correctAnswers = 0
    private var isPracticeMode = false
    private var timer: CountDownTimer? = null

    private val _question = MutableLiveData<String>()
    val question: LiveData<String> get() = _question

    private val _score = MutableLiveData<String>()
    val score: LiveData<String> get() = _score

    private val _timer = MutableLiveData<String>()
    val timerText: LiveData<String> get() = _timer

    private val _alert = MutableLiveData<String>()
    val alert: LiveData<String> get() = _alert

    private var levelRange = 10

    fun startGame(level: String) {
        isPracticeMode = false
        levelRange = when (level) {
            "easy" -> 10
            "medium" -> 50
            "hard" -> 100
            else -> 10
        }
        resetGame()
        startTimer()
        generateQuestion()
    }

    fun startPracticeMode() {
        isPracticeMode = true
        resetGame()
        generateQuestion()
    }

    private fun resetGame() {
        correctAnswers = 0
        totalQuestions = 0
        _score.value = "0/0"
        _alert.value = "Let's Do It!"
        timer?.cancel()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timer.value = "${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                _alert.value = "Time's up!"
            }
        }.start()
    }

    fun checkAnswer(selected: Int) {
        totalQuestions++
        if (selected == correctAnswer) {
            correctAnswers++
            _alert.value = "Correct!"
        } else {
            _alert.value = "Wrong!"
        }
        _score.value = "$correctAnswers/$totalQuestions"
        generateQuestion()
    }

    private fun generateQuestion() {
        val a = Random.nextInt(0, levelRange)
        val b = Random.nextInt(0, levelRange)
        correctAnswer = a + b
        _question.value = "$a + $b"
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}
