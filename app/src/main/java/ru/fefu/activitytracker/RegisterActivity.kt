package ru.fefu.activitytracker

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import ru.fefu.activitytracker.databinding.RegisterActivityBinding


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: RegisterActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.myToolbar)

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
    }
}