package com.example.kidsmathsgame

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kidsmathsgame.databinding.ActivityPlayBinding

class PlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inisialisasi ViewBinding
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set listener untuk tombol operasi
        binding.btnPlus.setOnClickListener { startQuiz("+") }
        binding.btnMinus.setOnClickListener { startQuiz("-") }
        binding.btnMultiply.setOnClickListener { startQuiz("×") }
        binding.btnDivide.setOnClickListener { startQuiz("÷") }
    }

    private fun startQuiz(op: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("cals", op)
        startActivity(intent)
    }
}
