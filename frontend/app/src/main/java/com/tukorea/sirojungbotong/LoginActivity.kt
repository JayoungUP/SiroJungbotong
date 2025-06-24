package com.tukorea.sirojungbotong

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.tukorea.sirojungbotong.databinding.LoginBinding
import com.tukorea.sirojungbotong.util.PreferenceUtil
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private var isPasswordVisible = false

    // 요청 데이터 클래스
    data class LoginRequest(
        val loginId: String,
        val password: String
    )

    // 응답 데이터 클래스
    data class LoginWrapperResponse(
        val status: Int,
        val data: LoginResponse
    )

    data class LoginResponse(
        val nickname: String,
        val accessToken: String,
        val refreshToken: String,
        val role: String
    )

    // Retrofit 인터페이스
    interface ApiService {
        @POST("/api/auth/login")
        fun login(@Body request: LoginRequest): Call<LoginWrapperResponse>

        @POST("/api/auth/login/kakao")
        fun kakaoLogin(@Body accessToken: String): Call<LoginWrapperResponse>
    }

    // Retrofit 객체 생성
    private val retrofit: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://sirojungbotong.r-e.kr")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // 카카오 로그인 성공 시 서버에 토큰 전송
    private fun sendKakaoAccessTokenToServer(accessToken: String) {
        retrofit.kakaoLogin("\"$accessToken\"").enqueue(object : Callback<LoginWrapperResponse> {
            override fun onResponse(call: Call<LoginWrapperResponse>, response: Response<LoginWrapperResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()?.data
                    if (result != null) {
                        // SharedPreferences 저장
                        PreferenceUtil.setAccessToken(this@LoginActivity, result.accessToken)
                        PreferenceUtil.setRefreshToken(this@LoginActivity, result.refreshToken)
                        PreferenceUtil.setNickname(this@LoginActivity, result.nickname)
                        PreferenceUtil.setRole(this@LoginActivity, result.role)

                        Log.d("KakaoServerLogin", "nickname: ${result.nickname}")
                        Log.d("KakaoServerLogin", "accessToken: ${result.accessToken}")
                        Log.d("KakaoServerLogin", "refreshToken: ${result.refreshToken}")
                        Log.d("KakaoServerLogin", "role: ${result.role}")
                        Toast.makeText(this@LoginActivity, "${result.nickname}님 환영합니다!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Log.e("KakaoServerLogin", "응답 본문이 비어 있음")
                    }
                } else {
                    Log.e("KakaoServerLogin", "응답 실패: ${response.code()} - ${response.errorBody()?.string()}")
                    Toast.makeText(this@LoginActivity, "카카오 로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginWrapperResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                Log.e("KakaoServerLogin", "서버 통신 실패", t)
            }
        })
    }

    // 카카오 로그인 함수
    fun loginWithKakaoAccount() {
        UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
            if (error != null) {
                Log.e("KakaoLogin", "카카오계정 로그인 실패: $error")
                Toast.makeText(this, "카카오 로그인 실패", Toast.LENGTH_SHORT).show()
            } else if (token != null) {
                Log.d("KakaoLogin", "카카오계정 로그인 성공: ${token.accessToken}")
                sendKakaoAccessTokenToServer(token.accessToken)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoSdk.init(this, "2ded46be765ec69de3b8d15cbfbe3cb4")
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ← 뒤로가기
        binding.btnBack.setOnClickListener { finish() }

        // 비밀번호 보기 토글
        binding.btnTogglePw.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.editPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.editPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.editPassword.setSelection(binding.editPassword.text.length)
        }

        // 아이디/비밀번호 찾기
        binding.tvFindId.setOnClickListener {
            val intent = Intent(this, FindActivity::class.java)
            intent.putExtra("mode", "id")
            startActivity(intent)
        }

        binding.tvFindPw.setOnClickListener {
            val intent = Intent(this, FindActivity::class.java)
            intent.putExtra("mode", "pw")
            startActivity(intent)
        }

        // 이메일 로그인 버튼
        binding.btnLogin.setOnClickListener {
            val id = binding.editId.text.toString()
            val pw = binding.editPassword.text.toString()

            binding.tvIdError.visibility = View.GONE
            binding.tvIdLabel.setTextColor(Color.BLACK)
            binding.tvPwLabel.setTextColor(Color.BLACK)
            binding.editId.setBackgroundResource(R.drawable.edit_text_background)
            (binding.editPassword.parent as View).setBackgroundResource(R.drawable.edit_text_background)

            if (id.isBlank() || pw.isBlank()) {
                Toast.makeText(this, "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(id, pw)

            retrofit.login(loginRequest).enqueue(object : Callback<LoginWrapperResponse> {
                override fun onResponse(call: Call<LoginWrapperResponse>, response: Response<LoginWrapperResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()?.data
                        if (result != null) {
                            // SharedPreferences 저장
                            PreferenceUtil.setAccessToken(this@LoginActivity, result.accessToken)
                            PreferenceUtil.setRefreshToken(this@LoginActivity, result.refreshToken)
                            PreferenceUtil.setNickname(this@LoginActivity, result.nickname)
                            PreferenceUtil.setRole(this@LoginActivity, result.role)

                            Log.d("LoginSuccess", "nickname: ${result.nickname}")
                            Log.d("LoginSuccess", "accessToken: ${result.accessToken}")
                            Log.d("LoginSuccess", "refreshToken: ${result.refreshToken}")
                            Log.d("LoginSuccess", "role: ${result.role}")
                            Toast.makeText(this@LoginActivity, "${result.nickname}님 환영합니다!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()
                        } else {
                            Log.e("LoginSuccess", "data is null")
                        }
                    } else {
                        Log.e("LoginError", "response failed: ${response.code()} - ${response.errorBody()?.string()}")
                        binding.tvIdError.visibility = View.VISIBLE
                        binding.tvIdError.text = "아이디 또는 비밀번호가 일치하지 않습니다."
                        binding.tvIdLabel.setTextColor(Color.RED)
                        binding.tvPwLabel.setTextColor(Color.RED)
                        binding.editId.setBackgroundResource(R.drawable.edit_text_background_error)
                        (binding.editPassword.parent as View).setBackgroundResource(R.drawable.edit_text_background_error)
                    }
                }

                override fun onFailure(call: Call<LoginWrapperResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "서버 오류: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("LoginFailure", "네트워크 실패", t)
                }
            })
        }

        // 카카오 로그인 버튼 클릭
        binding.btnKakaoLogin.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e("KakaoLogin", "카카오톡 로그인 실패: $error")
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) return@loginWithKakaoTalk
                        loginWithKakaoAccount()
                    } else if (token != null) {
                        Log.d("KakaoLogin", "카카오톡 로그인 성공: ${token.accessToken}")
                        sendKakaoAccessTokenToServer(token.accessToken)
                    }
                }
            } else {
                loginWithKakaoAccount()
            }
        }
    }
}
