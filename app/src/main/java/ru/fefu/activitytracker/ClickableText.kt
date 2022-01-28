package ru.fefu.activitytracker

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View

class ClickableText: ClickableSpan() {
    override fun onClick(widget: View) {
        Log.e("TAG", "onClick: askdf;lasjdflas", )
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }


}