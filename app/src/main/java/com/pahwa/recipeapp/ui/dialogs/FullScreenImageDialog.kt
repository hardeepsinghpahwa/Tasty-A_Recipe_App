package com.pahwa.recipeapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.pahwa.recipeapp.R
import com.pahwa.recipeapp.databinding.DialogFullScreenImageBinding

class FullScreenImageDialog : DialogFragment() {


    internal companion object {

        private var image = ""

        fun newInstance(image: String): FullScreenImageDialog {
            this.image = image
            return FullScreenImageDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding: DialogFullScreenImageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(
                context
            ), R.layout.dialog_full_screen_image, null, false
        )

        val circularProgressDrawable = CircularProgressDrawable(dialog?.context!!)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(binding.root.context)
            .load(image)
            .placeholder(circularProgressDrawable)
            .into(binding.img)

        binding.close.setOnClickListener {
            dialog?.dismiss()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        );
    }
}