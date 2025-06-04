package com.tukorea.sirojungbotong

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tukorea.sirojungbotong.databinding.LoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private var isPasswordVisible = false

    data class LoginRequest(
        val loginId: String,
        val password: String
    )

    data class LoginResponse(
        val nickname: String,
        val accessToken: String,
        val refreshToken: String,
        val role: String
    )

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

            // 초기화
            binding.tvIdError.visibility = View.GONE
            binding.tvIdLabel.setTextColor(Color.BLACK)
            binding.tvPwLabel.setTextColor(Color.BLACK)
            binding.editId.setBackgroundResource(R.drawable.edit_text_background)
            (binding.editPassword.parent as View).setBackgroundResource(R.drawable.edit_text_background)

            // 빈 입력 확인
            if (id.isBlank() || pw.isBlank()) {
                Toast.makeText(this, "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 로그인 실패 처리
            if (id != "admin" || pw != "1234") {
                binding.tvIdError.visibility = View.VISIBLE
                binding.tvIdError.text = "아이디 또는 비밀번호가 일치하지 않습니다."

                binding.tvIdLabel.setTextColor(Color.RED)
                binding.tvPwLabel.setTextColor(Color.RED)

                binding.editId.setBackgroundResource(R.drawable.edit_text_background_error)
                (binding.editPassword.parent as View).setBackgroundResource(R.drawable.edit_text_background_error)

                return@setOnClickListener
            }

            // 로그인 성공
            Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
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
