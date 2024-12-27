package com.example.readquran.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.readquran.databinding.ActivitySplachScreenBinding
import com.example.readquran.repo.local.db.LocalRepoImp
import com.example.readquran.ui.loading.LoadingActivity
import com.example.readquran.ui.main.MainActivity
import kotlinx.coroutines.runBlocking

class SplachScreen : AppCompatActivity() {
    private var length=0
    private lateinit var db: LocalRepoImp
    private lateinit var binding: ActivitySplachScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplachScreenBinding.inflate(layoutInflater)
        db= LocalRepoImp(this)

        runBlocking {
            length=db.getSize()
        }
        Handler().postDelayed({
            if (length==114){
                startActivity(Intent(this, MainActivity::class.java))
            }
            else{
                startActivity(Intent(this, LoadingActivity::class.java))
            }
            finish()

        },1000)



        setContentView(binding.root)

    }



}