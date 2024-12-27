package com.example.readquran.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.readquran.R
import com.example.readquran.databinding.ActivityMainBinding
import com.example.readquran.repo.local.shared.ShardPreference
import com.example.readquran.ui.Azkar.AzkarCategoryFragment
import com.example.readquran.ui.favourite.FavFragment
import com.example.readquran.ui.quran.QuranFragment
import com.example.readquran.ui.radio.RadioFragment
import com.example.readquran.ui.setting.SettingFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pref: ShardPreference
    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        replaceFrame(QuranFragment())
        pref = ShardPreference(this)
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentTime = System.currentTimeMillis()
                if (currentTime - backPressedTime < 2000) {
                    // Exit the app
                    backToast.cancel() // Dismiss the previous toast
                    finishAffinity()   // Close the app and all its activities
                } else {
                    // Notify the user
                    backPressedTime = currentTime
                    backToast = Toast.makeText(this@MainActivity, "اضغط مرة أخرى للخروج", Toast.LENGTH_SHORT)
                    backToast.show()
                }
            }

        })

        binding.mainNavigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.quran_nav_btn -> {
                    replaceFrame(QuranFragment())
                }

                R.id.setting_nav_btn -> {
                    replaceFrame(SettingFragment())

                }
                R.id.radio_nav_btn -> {
                    replaceFrame(RadioFragment())
                }

                R.id.fav_nav_btn -> {
                    replaceFrame(FavFragment())
                }
                R.id.zekr_nav_btn->{
                    replaceFrame(AzkarCategoryFragment())
                }
            }
            true
        }
    }

    private fun replaceFrame(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.mainFrameLayout, fragment)
            .commit()
    }
}


