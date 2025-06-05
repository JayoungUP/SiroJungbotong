package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FoundIdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.found_id)

        val userId = intent.getStringExtra("userId") ?: "알 수 없음"

        val tvFoundId = findViewById<TextView>(R.id.tv_found_id)
        tvFoundId.text = "가입하신 아이디는\n$userId 입니다."

        val btnGoLogin = findViewById<ImageView>(R.id.btn_go_login)
        btnGoLogin.setOnClickListener {
            finish()
        }
    }
}
