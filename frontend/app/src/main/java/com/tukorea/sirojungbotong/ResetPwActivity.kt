package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tukorea.sirojungbotong.databinding.ResetPwBinding

class ResetPwActivity : AppCompatActivity() {

    private lateinit var binding: ResetPwBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ResetPwBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 1) 저장하기 버튼 눌렀을 때 새 비밀번호 검증 및 처리
        binding.btnSave.setOnClickListener {
            val newPwd = binding.etNewPassword.text.toString().trim()
            val confirmPwd = binding.etConfirmPassword.text.toString().trim()

            // 비어 있는지 확인
            if (newPwd.isEmpty() || confirmPwd.isEmpty()) {
                Toast.makeText(this, "비밀번호를 모두 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 두 비밀번호가 일치하는지 확인
            if (newPwd != confirmPwd) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO: 실제 백엔드에 새 비밀번호 전송 로직을 구현하세요.
            // 예시:
            // sendNewPasswordToServer(newPwd)

            val intent = Intent(this, ResetPwEndActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * 백엔드 서버에 새 비밀번호를 전달하는 예시 함수 (구현부는 프로젝트 상황에 맞게 작성)
     */
    private fun sendNewPasswordToServer(password: String) {
        // 1) Retrofit / Volley 등을 이용해 API 호출
        // 2) 응답에 따라 성공/실패 토스트 처리
        // 3) 필요 시 ProgressBar, 로딩 UI 추가
    }
}
