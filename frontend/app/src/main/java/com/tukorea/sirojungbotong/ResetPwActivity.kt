package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tukorea.sirojungbotong.databinding.ResetPwBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class ResetPwActivity : AppCompatActivity() {

    private lateinit var binding: ResetPwBinding

    // 🔹 이메일은 이전 화면에서 전달받음
    private var email: String? = null

    // 🔸 요청 데이터 클래스
    data class ResetPasswordRequest(
        val email: String,
        val newPassword: String
    )

    // 🔸 Retrofit 인터페이스
    interface ApiService {
        @POST("/api/auth/password/reset")
        fun resetPassword(@Body request: ResetPasswordRequest): Call<Void>
    }

    // 🔸 Retrofit 객체 생성
    private val retrofit: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://sirojungbotong.r-e.kr")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ResetPwBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 전달받은 이메일 가져오기
        email = intent.getStringExtra("email")

        binding.btnSave.setOnClickListener {
            val newPwd = binding.etNewPassword.text.toString().trim()
            val confirmPwd = binding.etConfirmPassword.text.toString().trim()

            if (newPwd.isEmpty() || confirmPwd.isEmpty()) {
                Toast.makeText(this, "비밀번호를 모두 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPwd != confirmPwd) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 이메일이 null일 경우 방어
            val safeEmail = email ?: run {
                Toast.makeText(this, "이메일 정보가 없습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 실제 서버 요청
            sendNewPasswordToServer(safeEmail, newPwd)
        }
    }

    private fun sendNewPasswordToServer(email: String, newPassword: String) {
        val request = ResetPasswordRequest(email, newPassword)

        retrofit.resetPassword(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ResetPwActivity, "비밀번호가 변경되었습니다!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@ResetPwActivity, ResetPwEndActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@ResetPwActivity, "비밀번호 변경 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ResetPwActivity, "서버 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
