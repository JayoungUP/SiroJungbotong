package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tukorea.sirojungbotong.databinding.SingupEmailBinding  // XML 파일명에 맞춘 바인딩 이름
import com.example.yourapp.SignupExtraActivity  // 패키지명 확인 필요

class SignupEmailActivity : AppCompatActivity() {

    private lateinit var binding: SingupEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SingupEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 다음으로 버튼
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val nickname = binding.etNickname.text.toString().trim()
            val loginId = binding.etId.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            val email = binding.etEmail.text.toString().trim()

            // 유효성 검사
            if (name.isEmpty() || nickname.isEmpty() || loginId.isEmpty() || password.isEmpty()
                || confirmPassword.isEmpty() || email.isEmpty()
            ) {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 인텐트로 개별 값 전달
            val intent = Intent(this, SignupExtraActivity::class.java).apply {
                putExtra("name", name)
                putExtra("nickname", nickname)
                putExtra("loginId", loginId)
                putExtra("password", password)
                putExtra("email", email)
            }
            startActivity(intent)
        }
    }
}
