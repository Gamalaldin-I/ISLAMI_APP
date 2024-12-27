package com.example.readquran.util

import android.os.Handler
import android.os.Looper
import android.view.View

object Animator {
    fun openScale(view:View){
        view.animate().scaleX(1f).scaleY(1f).setDuration(500).start()
    }
    fun closeScale(view:View){
        view.animate().scaleX(0f).scaleY(0f).setDuration(500).start()
    }
    fun animateBtn(btn: View){
        btn.animate().alpha(1f).scaleX(1.2f).scaleY(1.2f).
        setDuration(200).withEndAction { btn.animate().alpha(1f).scaleX(0.4f).scaleY(0.4f) }.setDuration(100).start()
        Handler(Looper.getMainLooper()).postDelayed({
            btn.animate().alpha(1f).scaleX(1f).scaleY(1f).
            setDuration(400).start()
        },300)
    }
    fun animateAppearanceOfCardOfExoPlayer(view: View){
        view.animate().scaleY(0f).scaleX(0f).setDuration(0).start()
        view.animate().setStartDelay(100).scaleY(1f).scaleX(1f).setDuration(500).start()
    }
    fun animateClosingCardOfExoPlayer(view: View){
            view.animate().setStartDelay(100).scaleY(0f).scaleX(0f).setDuration(500).withEndAction{
            view.visibility= View.GONE

        }.start()

    }
     fun showToolbar(toolbar: View) {
        toolbar.animate().translationY(-toolbar.height.toFloat()).setDuration(0).start()
        toolbar.visibility = View.VISIBLE
        toolbar.animate()
            .translationY(0f) // Reset to original position (visible)
            .setDuration(200).withEndAction{
            } // Animation duration in milliseconds
            .start()
    }


     fun hideToolbar(toolbar: View) {
        toolbar.animate()
            .translationY(-toolbar.height.toFloat()) // Move it above the screen
            .setDuration(200).withEndAction{
                toolbar.visibility=View.GONE
            }// Animation duration in milliseconds
            .start()
    }
}