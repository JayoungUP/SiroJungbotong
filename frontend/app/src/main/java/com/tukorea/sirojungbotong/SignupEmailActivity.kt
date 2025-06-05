package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.yourapp.SignupExtraActivity

class SignupEmailActivity : AppCompatActivity() {

    private lateinit var nicknameEditText: EditText
    private lateinit var idEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var registerButton: ImageButton
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.singup_email) // XML 파일 이름에 따라 수정

        // 뷰 초기화
        nicknameEditText = findViewById(R.id.et_nickname)
        idEditText = findViewById(R.id.et_id)
        passwordEditText = findViewById(R.id.et_password)
        confirmPasswordEditText = findViewById(R.id.et_confirm_password)
        emailEditText = findViewById(R.id.et_email)
        registerButton = findViewById(R.id.btn_register)
        backButton = findViewById(R.id.btn_back)

        // 뒤로가기
        backButton.setOnClickListener {
            finish()
        }

        // 가입 완료 버튼 클릭
        registerButton.setOnClickListener {
            val nickname = nicknameEditText.text.toString().trim()
            val userId = idEditText.text.toString().trim()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val email = emailEditText.text.toString().trim()

            if (nickname.isEmpty() || userId.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO: 서버에 회원가입 요청 전송
            Toast.makeText(this, "회원가입 요청 전송됨 (예시)", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SignupExtraActivity::class.java)
            startActivity(intent)
        }
    }
}
