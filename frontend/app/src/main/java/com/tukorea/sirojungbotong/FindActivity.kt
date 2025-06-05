package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tukorea.sirojungbotong.databinding.FindCommonBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class FindActivity : AppCompatActivity() {

    private lateinit var binding: FindCommonBinding

    // 요청/응답 데이터 클래스
    data class FindIdRequest(val email: String)
    data class VerifyCodeRequest(val email: String, val code: String)
    data class VerifyCodeResponse(val loginId: String)
    data class ResetPwEmailRequest(val email: String)
    data class VerifyResetPwRequest(val email: String, val code: String)

    // 🔸 중첩 응답용 래퍼 클래스
    data class VerifyCodeWrapperResponse(val status: Int, val data: VerifyCodeResponse)

    // Retrofit 인터페이스
    interface ApiService {
        @POST("/api/auth/id/find")
        fun requestVerificationCode(@Body request: FindIdRequest): Call<Void>

        @POST("/api/auth/id/verify")
        fun verifyCode(@Body request: VerifyCodeRequest): Call<VerifyCodeWrapperResponse>

        @POST("/api/auth/password/findByEmail")
        fun sendResetCode(@Body request: ResetPwEmailRequest): Call<Void>

        @POST("/api/auth/password/verify")
        fun verifyResetCode(@Body request: VerifyResetPwRequest): Call<Void>
    }

    private val retrofit: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://sirojungbotong.r-e.kr")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FindCommonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        val mode = intent.getStringExtra("mode") ?: "id"
        if (mode == "pw") showPwForm() else showIdForm()

        binding.btnTabId.setOnClickListener { showIdForm() }
        binding.btnTabPw.setOnClickListener { showPwForm() }
    }

    private fun showIdForm() {
        binding.btnTabId.setBackgroundResource(R.drawable.tab_background_white)
        binding.btnTabId.setTextColor(getColor(R.color.black))
        binding.btnTabPw.setBackgroundColor(android.graphics.Color.TRANSPARENT)
        binding.btnTabPw.setTextColor(getColor(R.color.gray02))

        binding.formContainer.removeAllViews()
        val idForm = layoutInflater.inflate(R.layout.layout_find_id_form, binding.formContainer, false)
        binding.formContainer.addView(idForm)

        val emailEdit = idForm.findViewById<EditText>(R.id.edit_email)
        val sendBtn = idForm.findViewById<TextView>(R.id.btn_send_email)
        val codeEdit = idForm.findViewById<EditText>(R.id.edit_code)
        val checkCodeBtn = idForm.findViewById<TextView>(R.id.btn_check_code)

        sendBtn.setOnClickListener {
            val email = emailEdit.text.toString()
            if (email.isBlank()) {
                Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            retrofit.requestVerificationCode(FindIdRequest(email)).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@FindActivity, "인증번호를 이메일로 전송했습니다", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@FindActivity, "이메일 전송 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@FindActivity, "서버 오류: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        checkCodeBtn.setOnClickListener {
            val email = emailEdit.text.toString()
            val inputCode = codeEdit.text.toString()
            if (email.isBlank() || inputCode.isBlank()) {
                Toast.makeText(this, "이메일과 인증번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            retrofit.verifyCode(VerifyCodeRequest(email, inputCode))
                .enqueue(object : Callback<VerifyCodeWrapperResponse> {
                    override fun onResponse(call: Call<VerifyCodeWrapperResponse>, response: Response<VerifyCodeWrapperResponse>) {
                        if (response.isSuccessful) {
                            val userId = response.body()?.data?.loginId ?: "알 수 없음"
                            val intent = Intent(this@FindActivity, FoundIdActivity::class.java)
                            intent.putExtra("userId", userId)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@FindActivity, "인증번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<VerifyCodeWrapperResponse>, t: Throwable) {
                        Toast.makeText(this@FindActivity, "서버 오류: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun showPwForm() {
        binding.btnTabPw.setBackgroundResource(R.drawable.tab_background_white)
        binding.btnTabPw.setTextColor(getColor(R.color.black))
        binding.btnTabId.setBackgroundColor(android.graphics.Color.TRANSPARENT)
        binding.btnTabId.setTextColor(getColor(R.color.gray02))

        binding.formContainer.removeAllViews()
        val pwForm = layoutInflater.inflate(R.layout.layout_find_pw_form, binding.formContainer, false)
        binding.formContainer.addView(pwForm)

        val emailEdit = pwForm.findViewById<EditText>(R.id.edit_email)
        val codeEdit = pwForm.findViewById<EditText>(R.id.edit_code)
        val sendBtn = pwForm.findViewById<TextView>(R.id.btn_send_email)
        val checkCodeBtn = pwForm.findViewById<TextView>(R.id.btn_check_code)

        sendBtn.setOnClickListener {
            val email = emailEdit.text.toString()
            if (email.isBlank()) {
                Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            retrofit.sendResetCode(ResetPwEmailRequest(email)).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@FindActivity, "인증번호 전송 완료", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@FindActivity, "등록되지 않은 이메일이거나 오류입니다", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@FindActivity, "서버 오류: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        checkCodeBtn.setOnClickListener {
            val email = emailEdit.text.toString()
            val code = codeEdit.text.toString()

            if (email.isBlank() || code.isBlank()) {
                Toast.makeText(this, "이메일과 인증번호를 모두 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            retrofit.verifyResetCode(VerifyResetPwRequest(email, code)).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        val intent = Intent(this@FindActivity, ResetPwActivity::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@FindActivity, "인증번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@FindActivity, "서버 오류: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
