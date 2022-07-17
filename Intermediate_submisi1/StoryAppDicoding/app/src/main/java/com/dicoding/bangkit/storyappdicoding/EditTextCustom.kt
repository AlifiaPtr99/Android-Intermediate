package com.dicoding.bangkit.storyappdicoding

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText

class EditTextCustom : AppCompatEditText{
    var inputData = ""

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(inputData == "password") {
                    if(s.length in 1..5) {
                        error = context.getString(R.string.password_error)
                    }
                } else if(inputData == "email") {
                    if(!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                        error = context.getString(R.string.email_error)
                    }
                } else {
                    if(s.isEmpty()) {
                        error = context.getString(R.string.name_error)
                    }
                }


            }

            override fun afterTextChanged(s: Editable?) {
                // nothing
            }
        })
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
}