package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.tukorea.sirojungbotong.databinding.SignupKakaoBinding

class SignupKakaoActivity : AppCompatActivity() {

    private lateinit var binding: SignupKakaoBinding

    // 🔹 이메일 로그인 함수
    fun loginWithKakaoAccount() {
        UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
            if (error != null) {
                Log.e("KakaoLogin", "카카오계정 로그인 실패: $error")
                Toast.makeText(this, "카카오 로그인 실패", Toast.LENGTH_SHORT).show()
            } else if (token != null) {
                Log.d("KakaoLogin", "카카오계정 로그인 성공: ${token.accessToken}")
                Toast.makeText(this, "카카오 로그인 성공!", Toast.LENGTH_SHORT).show()
                // TODO: 서버에 토큰 보내서 가입/로그인 처리
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoSdk.init(this, "2ded46be765ec69de3b8d15cbfbe3cb4")

        binding = SignupKakaoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 카카오 버튼 클릭 시 이동
        binding.btnKakao.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                // ✅ 카카오톡 앱으로 로그인
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e("KakaoLogin", "카카오톡 로그인 실패: $error")

                        // 사용자가 로그인 취소한 경우
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 다른 오류 → 카카오 계정(웹) 로그인 시도
                        loginWithKakaoAccount()
                    } else if (token != null) {
                        Log.d("KakaoLogin", "카카오톡 로그인 성공: ${token.accessToken}")
                        Toast.makeText(this, "카카오 로그인 성공!", Toast.LENGTH_SHORT).show()
                        // TODO: 서버에 토큰 보내서 가입/로그인 처리
                    }
                }
            } else {
                // ✅ 카카오 계정(웹) 로그인
                loginWithKakaoAccount()
            }
        }

        // 이메일 버튼 클릭 시 이동
        binding.btnEmail.setOnClickListener {
            val intent = Intent(this, SignupEmailActivity::class.java)
            startActivity(intent)
        }

        // 뒤로가기 버튼 클릭 시 이전 화면으로 종료
        binding.btnBack.setOnClickListener {
            finish() // 현재 액티비티 종료 = 이전 화면으로 돌아감
        }
    }
}
