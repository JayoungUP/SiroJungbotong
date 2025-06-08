package com.tukorea.sirojungbotong

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ResetPwEndActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reset_pw_end)

        val btnGoLogin = findViewById<ImageView>(R.id.btn_go_login)
        btnGoLogin.setOnClickListener {
            finish()
        }
    }
}
