package ru.fefu.activitytracker

import android.app.Application
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat

class ClickableText: ClickableSpan() {
    override fun onClick(widget: View) {
        Log.e("TAG", "onClick: askdf;lasjdflas", )
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }


}