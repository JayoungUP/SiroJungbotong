package com.tukorea.sirojungbotong

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tukorea.sirojungbotong.databinding.SignupKakaoBinding

class SignupKakaoActivity : AppCompatActivity() {

    private lateinit var binding: SignupKakaoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SignupKakaoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnKakao.setOnClickListener {
            Toast.makeText(this, "카카오 로그인 시작", Toast.LENGTH_SHORT).show()
        }
    }
}
