package ru.fefu.activitytracker

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import ru.fefu.activitytracker.databinding.WelcomeScreenBinding

class WelcomeScreenActivity : AppCompatActivity() {

    private lateinit var binding: WelcomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val haveAccountText = SpannableString("Уже есть аккаунт?")
        val haveAnAccountStringClick = object : ClickableSpan() {
            override fun onClick(view: View) {
                Toast.makeText(view.context, "Clicked: on Text", Toast.LENGTH_LONG).show()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ResourcesCompat.getColor(resources, R.color.purple_700, null)
                ds.linkColor = ResourcesCompat.getColor(resources, R.color.purple_700, null)
                ds.isUnderlineText = false
            }
        }
        haveAccountText.setSpan(
            haveAnAccountStringClick,
            0,
            haveAccountText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.haveAnAccountMessage.text = haveAccountText
        binding.haveAnAccountMessage.movementMethod = LinkMovementMethod.getInstance()
        binding.haveAnAccountMessage.highlightColor = Color.TRANSPARENT
    }
}