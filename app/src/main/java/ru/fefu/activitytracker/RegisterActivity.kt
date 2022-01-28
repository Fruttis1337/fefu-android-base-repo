package ru.fefu.activitytracker

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.fefu.activitytracker.Retrofit.Result
import ru.fefu.activitytracker.data.TokenUserModel
import ru.fefu.activitytracker.data.enum.GenderEnum
import ru.fefu.activitytracker.databinding.RegisterActivityBinding
import ru.fefu.activitytracker.viewmodels.SignUpViewModel


class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: RegisterActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterActivityBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        setContentView(binding.root)
        setSupportActionBar(binding.myToolbar)

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

        binding.regButton.setOnClickListener {
            val login = binding.login.editText?.text.toString()
            val password = binding.password.editText?.text.toString()
            val name = binding.nickname.editText?.text.toString()
            val gender = findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId).text.toString()
            var genderOrdinal = 0
            for (i in enumValues<GenderEnum>()) {
                if (i.type == gender) {
                    genderOrdinal = i.ordinal
                }
            }
            viewModel.register(login, password, name, genderOrdinal)
        }

        val string = SpannableString(getString(R.string.accepted_personal_data))
        string.setSpan(
            ClickableText(),
            string.indexOf("политикой конфиденциальности"),
            string.indexOf("политикой конфиденциальности") + "политикой конфиденциальности".length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        string.setSpan(
            ClickableText(),
            string.indexOf("пользовательское соглашение", 0),
            string.lastIndex + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.acceptedPersonalData.movementMethod = LinkMovementMethod.getInstance()
        binding.acceptedPersonalData.highlightColor = 0
        binding.acceptedPersonalData.text = string

        binding.myToolbar.setNavigationOnClickListener {
            this.finish()
        }
    }
}