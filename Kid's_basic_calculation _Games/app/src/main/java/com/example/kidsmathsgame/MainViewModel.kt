package com.example.kidsmathgame

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val _question = MutableLiveData<String>()
    val question: LiveData<String> = _question

    private val _options = MutableLiveData<List<Int>>()
    val options: LiveData<List<Int>> = _options

    private val _score = MutableLiveData<Pair<Int, Int>>()
    val score: LiveData<Pair<Int, Int>> = _score

    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String> = _timerText

    private val _feedback = MutableLiveData<String>()
    val feedback: LiveData<String> = _feedback

    private var correctAnswer = 0
    private var timer: CountDownTimer? = null

    init {
        _score.value = Pair(0, 0)
        startGame()
    }

    private fun startGame() {
        startTimer()
        generateNewQuestion()
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timerText.value = "${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                _timerText.value = "Time's up!"
                _feedback.value = "Game Over"
            }
        }.start()
    }

    fun generateNewQuestion() {
        val a = Random.nextInt(10, 50)
        val b = Random.nextInt(1, 20)
        correctAnswer = a + b
        _question.value = "$a + $b"

        val answers = mutableSetOf(correctAnswer)
        while (answers.size < 2) {
            val wrong = correctAnswer + Random.nextInt(-10, 10)
            if (wrong != correctAnswer) answers.add(wrong)
        }
        _options.value = answers.shuffled()
    }

    fun chooseAnswer(answer: Int) {
        val (correct, total) = _score.value ?: Pair(0, 0)
        if (answer == correctAnswer) {
            _score.value = Pair(correct + 1, total + 1)
            _feedback.value = "Correct!"
        } else {
            _score.value = Pair(correct, total + 1)
            _feedback.value = "Wrong!"
        }
        generateNewQuestion()
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}
