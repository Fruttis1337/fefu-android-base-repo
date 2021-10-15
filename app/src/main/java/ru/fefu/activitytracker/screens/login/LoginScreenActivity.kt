package ru.fefu.activitytracker.screens.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.fefu.activitytracker.databinding.ActivityLoginScreenBinding

class LoginScreenActivity: AppCompatActivity() {
    private lateinit var _binding: ActivityLoginScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.goBackButton.setOnClickListener {
            finish()
        }
    }
}
