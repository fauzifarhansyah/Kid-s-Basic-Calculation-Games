package com.example.kidsmathgame

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.kidsmathgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupOptionButtons()
    }

    private fun setupObservers() {
        viewModel.question.observe(this, Observer {
            binding.QuestionTextView.text = it
        })

        viewModel.options.observe(this, Observer { options ->
            val buttons = listOf(binding.button, binding.button2, binding.button3, binding.button4, binding.button5)
            for ((btn, opt) in buttons.zip(options)) {
                btn.text = opt.toString()
                btn.setOnClickListener { viewModel.chooseAnswer(opt) }
            }
        })

        viewModel.score.observe(this, Observer { (correct, total) ->
            binding.scoreTestView2.text = "$correct/$total"
        })

        viewModel.timerText.observe(this, Observer {
            binding.ScoreTextView.text = it
        })

        viewModel.feedback.observe(this, Observer {
            binding.AlertTextview.text = it
        })
    }

    private fun setupOptionButtons() {
        // Already handled in observer
    }
}
