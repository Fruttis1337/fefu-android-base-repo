package ru.fefu.activitytracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.fefu.activitytracker.Retrofit.Result
import ru.fefu.activitytracker.data.TokenUserModel
import ru.fefu.activitytracker.databinding.ActivityHaveAnAccountBinding
import ru.fefu.activitytracker.viewmodels.LoginViewModel


class HaveAnAccount : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityHaveAnAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHaveAnAccountBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setContentView(binding.root)

        viewModel.dataFlow
            .onEach {
                if (it is Result.Success<TokenUserModel>) {
                    App.INSTANCE.sharedPrefs.edit().putString("token", it.result.token).apply()
                    val intent = Intent(this, TrackerActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                else if (it is Result.Error<TokenUserModel>) {
                    Toast.makeText(this, it.e.toString(), Toast.LENGTH_LONG).show()
                }
            }
            .launchIn(lifecycleScope)

        binding.authButton.setOnClickListener {
            val login = binding.login.editText?.text.toString()
            val password = binding.password.editText?.text.toString()
            viewModel.login(login, password)
        }

        binding.myToolbar.setNavigationOnClickListener {
            this.finish()
        }
    }
}