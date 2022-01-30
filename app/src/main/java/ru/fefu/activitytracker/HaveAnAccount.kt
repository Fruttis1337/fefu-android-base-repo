package ru.fefu.activitytracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.fefu.activitytracker.databinding.ActivityHaveAnAccountBinding

class HaveAnAccount : AppCompatActivity() {

    private lateinit var binding: ActivityHaveAnAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHaveAnAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myToolbar.setNavigationOnClickListener {
            this.finish()
        }
    }
}