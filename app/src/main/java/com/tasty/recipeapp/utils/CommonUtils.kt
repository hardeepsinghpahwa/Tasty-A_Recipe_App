package com.tasty.recipeapp.utils

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.tasty.recipeapp.R


class CommonUtils {

    companion object {

        fun hideKeyboard(activity: Activity) {
            val imm: InputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            if (imm.isAcceptingText) {
                if (activity.currentFocus != null) {
                    val view: View = activity.currentFocus!!
                    //If no view currently has focus, create a new one, just so we can grab a window token from it

                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }

            }

        }

        fun showKeyboard(activity: Activity) {
            (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            );
        }


        fun showSuccessSnackBar(msg: String, view: View) {
            val snack = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
            val text: TextView =
                snack.view.findViewById(com.google.android.material.R.id.snackbar_text)
            text.setTextColor(ContextCompat.getColor(view.context, R.color.white))
            val view = snack.view
            val params = view.layoutParams as FrameLayout.LayoutParams
            snack.view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.tealGreen))
            params.gravity = Gravity.TOP
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            view.layoutParams = params
            snack.show()
        }

        fun showFailureSnackBar(msg: String, view: View) {
            val snack = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
            val text: TextView =
                snack.view.findViewById(com.google.android.material.R.id.snackbar_text)
            text.setTextColor(ContextCompat.getColor(view.context, R.color.white))
            val view = snack.view
            val params = view.layoutParams as FrameLayout.LayoutParams
            snack.view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.red))
            params.gravity = Gravity.TOP
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            view.layoutParams = params
            snack.show()
        }


    }
}