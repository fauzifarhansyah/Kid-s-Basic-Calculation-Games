package com.example.kidsmathgame

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

enum class QuizLevel(val range: IntRange) {
    EASY(1..10),
    MEDIUM(10..50),
    HARD(50..100)
}

class QuizViewModel : ViewModel() {

    private val _question = MutableLiveData<String>()
    val question: LiveData<String> = _question

    private val _timer = MutableLiveData<String>()
    val timer: LiveData<String> = _timer

    private val _score = MutableLiveData<String>()
    val score: LiveData<String> = _score

    private val _alert = MutableLiveData<String>()
    val alert: LiveData<String> = _alert

    private var correctAnswerIndex = 0
    private var correct = 0
    private var total = 0

    private var level = QuizLevel.EASY
    private var timerRunning = false
    private var isPracticeMode = false

    private lateinit var countDownTimer: CountDownTimer

    fun setLevel(newLevel: QuizLevel) {
        level = newLevel
        startGame()
    }

    fun startPracticeMode() {
        isPracticeMode = true
        startGame()
    }

    fun startGame() {
        correct = 0
        total = 0
        updateScore()
        if (!isPracticeMode) startTimer()
        generateQuestion()
        _alert.value = "Let's Do It!"
    }

    private fun startTimer() {
        if (timerRunning) return
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timer.value = "${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                timerRunning = false
                _alert.value = "Time's up! Score: $correct/$total"
            }
        }
        timerRunning = true
        countDownTimer.start()
    }

    private fun generateQuestion() {
        val a = level.range.random()
        val b = level.range.random()
        val answer = a + b
        correctAnswerIndex = Random.nextInt(4)

        val options = mutableListOf<Int>()
        for (i in 0..3) {
            if (i == correctAnswerIndex) {
                options.add(answer)
            } else {
                var wrongAnswer: Int
                do {
                    wrongAnswer = level.range.random() + level.range.random()
                } while (wrongAnswer == answer || options.contains(wrongAnswer))
                options.add(wrongAnswer)
            }
        }

        _question.value = "$a + $b"
        optionList = options
    }

    private var optionList: List<Int> = emptyList()

    fun checkAnswer(index: Int) {
        total++
        if (index == correctAnswerIndex) {
            correct++
            _alert.value = "Correct!"
        } else {
            _alert.value = "Wrong!"
        }
        updateScore()
        generateQuestion()
    }

    private fun updateScore() {
        _score.value = "$correct/$total"
    }

    override fun onCleared() {
        super.onCleared()
        if (::countDownTimer.isInitialized) countDownTimer.cancel()
    }
}
