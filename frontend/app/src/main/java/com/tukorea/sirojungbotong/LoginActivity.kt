package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tukorea.sirojungbotong.databinding.LoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ← 뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 카카오 로그인 버튼
        binding.btnKakaoLogin.setOnClickListener {
            Toast.makeText(this, "카카오 로그인 버튼 클릭.", Toast.LENGTH_SHORT).show()
            // val intent = Intent(this, KakaoLoginActivity::class.java)
            // startActivity(intent)
        }

        // 로그인 버튼
        binding.btnLogin.setOnClickListener {
            val id = binding.editId.text.toString()
            val pw = binding.editPassword.text.toString()

            if (id.isBlank() || pw.isBlank()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 실제 로그인 처리
                Toast.makeText(this, "로그인 성공 (임시)", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        // 비밀번호 보기/숨기기 토글
        binding.btnTogglePw.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.editPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.editPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.editPassword.setSelection(binding.editPassword.text.length)
        }

        // 아이디 찾기
        binding.tvFindId.setOnClickListener {
            Toast.makeText(this, "아이디 찾기 버튼 클릭", Toast.LENGTH_SHORT).show()
            // val intent = Intent(this, FindIdActivity::class.java)
            // startActivity(intent)
        }

        // 비밀번호 찾기
        binding.tvFindPw.setOnClickListener {
            Toast.makeText(this, "비밀번호를 찾기 버튼 클릭", Toast.LENGTH_SHORT).show()
            // val intent = Intent(this, FindPasswordActivity::class.java)
            // startActivity(intent)
        }
    }
}
