package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.yourapp.SignupExtraActivity
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.tukorea.sirojungbotong.databinding.SignupKakaoBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class SignupKakaoActivity : AppCompatActivity() {

    private lateinit var binding: SignupKakaoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoSdk.init(this, "2ded46be765ec69de3b8d15cbfbe3cb4")
        binding = SignupKakaoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnKakao.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                // ✅ 카카오톡 앱으로 로그인
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e("KakaoLogin", "카카오톡 로그인 실패: $error")
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) return@loginWithKakaoTalk
                        loginWithKakaoAccount()
                    } else if (token != null) {
                        Log.d("KakaoLogin", "카카오톡 로그인 성공: ${token.accessToken}")
                        fetchUserInfoAndSignup(token.accessToken)
                    }
                }
            } else {
                loginWithKakaoAccount()
            }
        }

        binding.btnEmail.setOnClickListener {
            val intent = Intent(this, SignupEmailActivity::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loginWithKakaoAccount() {
        UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
            if (error != null) {
                Log.e("KakaoLogin", "카카오계정 로그인 실패: $error")
                Toast.makeText(this, "카카오 로그인 실패", Toast.LENGTH_SHORT).show()
            } else if (token != null) {
                Log.d("KakaoLogin", "카카오계정 로그인 성공: ${token.accessToken}")
                fetchUserInfoAndSignup(token.accessToken)
            }
        }
    }

    // ✅ 사용자 정보 조회 후 서버로 전송
    private fun fetchUserInfoAndSignup(accessToken: String) {
        UserApiClient.instance.me { user: User?, error: Throwable? ->
            if (error != null || user == null) {
                Toast.makeText(this, "사용자 정보 조회 실패", Toast.LENGTH_SHORT).show()
                Log.e("KakaoUserInfo", "사용자 정보 조회 실패: $error")
                return@me
            }

            val name = user.kakaoAccount?.name ?: "Unknown"
            val nickname = user.properties?.get("nickname") ?: "NoNickname"

            val request = KakaoSignupRequest(
                kakaoAccessToken = accessToken,
                name = name,
                nickname = nickname
            )

            RetrofitClient.instance.signupWithKakao(request)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@SignupKakaoActivity, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignupKakaoActivity, SignupExtraActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@SignupKakaoActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                            Log.e("SignupResponse", "Code: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@SignupKakaoActivity, "서버 통신 오류", Toast.LENGTH_SHORT).show()
                        Log.e("SignupError", "에러: $t")
                    }
                })
        }
    }

    // 🔹 Retrofit 요청 바디
    data class KakaoSignupRequest(
        val kakaoAccessToken: String,
        val name: String,
        val nickname: String
    )

    // 🔹 Retrofit 인터페이스
    interface MemberService {
        @POST("/api/member/signup/user/kakao")
        fun signupWithKakao(@Body request: KakaoSignupRequest): Call<Void>
    }

    // 🔹 Retrofit 객체 생성
    object RetrofitClient {
        private const val BASE_URL = "http://sirojungbotong.r-e.kr"

        val instance: MemberService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(MemberService::class.java)
        }
    }
}
