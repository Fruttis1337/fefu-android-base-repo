package ru.fefu.activitytracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.fefu.activitytracker.databinding.ActivityHaveAnAccountBinding

class HaveAnAccount : AppCompatActivity() {

    private lateinit var binding: ActivityHaveAnAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHaveAnAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.authButton.setOnClickListener {
            val intent = Intent(this, TrackerActivity::class.java)
            startActivity(intent)
        }

        binding.myToolbar.setNavigationOnClickListener {
            this.finish()
        }
    }
}