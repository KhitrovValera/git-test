package com.example.gittest.ui.common.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.example.gittest.R
import com.google.android.material.button.MaterialButton


class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val button: MaterialButton
    private val progressBar: ProgressBar

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_loading_button, this, true)
        button = findViewById(R.id.btnInner)
        progressBar = findViewById(R.id.pbButton)
    }


    fun showLoading(isLoading: Boolean) {
        button.isEnabled = !isLoading
        progressBar.visibility = if (isLoading) VISIBLE else GONE
        button.text = if (isLoading) "" else resources.getString(R.string.sign_in)
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        button.setOnClickListener(listener)
    }

}