package com.tukorea.sirojungbotong

import android.content.Intent
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

        // 카카오 버튼 클릭 시 이동
        binding.btnKakao.setOnClickListener {
            Toast.makeText(this, "카카오 로그인 시작", Toast.LENGTH_SHORT).show()
            // val intent = Intent(this, KakaoLoginActivity::class.java)
            // startActivity(intent)
        }

        // 이메일 버튼 클릭 시 이동
        binding.btnEmail.setOnClickListener {
            Toast.makeText(this, "이메일 로그인 시작", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SignupEmailActivity::class.java)
            startActivity(intent)
        }

        // 뒤로가기 버튼 클릭 시 이전 화면으로 종료
        binding.btnBack.setOnClickListener {
            finish() // 현재 액티비티 종료 = 이전 화면으로 돌아감
        }
    }
}
