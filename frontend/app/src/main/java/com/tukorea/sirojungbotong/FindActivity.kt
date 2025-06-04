package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tukorea.sirojungbotong.databinding.FindCommonBinding

class FindActivity : AppCompatActivity() {

    private lateinit var binding: FindCommonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FindCommonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ← 뒤로가기
        binding.btnBack.setOnClickListener {
            finish()
        }

        // mode에 따라 초기 탭 표시
        val mode = intent.getStringExtra("mode") ?: "id"
        if (mode == "pw") {
            showPwForm()
        } else {
            showIdForm()
        }

        // 탭 전환 이벤트
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

        // 이메일 및 인증 처리 로직
        val emailEdit = idForm.findViewById<EditText>(R.id.edit_email)
        val sendBtn = idForm.findViewById<TextView>(R.id.btn_send_email)
        val codeEdit = idForm.findViewById<EditText>(R.id.edit_code)
        val checkCodeBtn = idForm.findViewById<TextView>(R.id.btn_check_code)

        var sentCode: String? = null

        sendBtn.setOnClickListener {
            val email = emailEdit.text.toString()
            if (email.isBlank()) {
                Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 실제 전송 로직 대신 테스트용 코드
            sentCode = "123456"
            Toast.makeText(this, "인증번호를 전송했습니다 (테스트 코드: $sentCode)", Toast.LENGTH_SHORT).show()
        }

        checkCodeBtn.setOnClickListener {
            val inputCode = codeEdit.text.toString()
            if (inputCode == sentCode) {
                Toast.makeText(this, "인증 완료", Toast.LENGTH_SHORT).show()

                val foundId = "example_user_id" // 실제 백엔드에서 받은 사용자 ID로 대체
                val intent = Intent(this, FoundIdActivity::class.java)
                intent.putExtra("userId", foundId)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "인증번호가 올바르지 않습니다", Toast.LENGTH_SHORT).show()
            }
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

        // 비밀번호 찾기 로직도 필요시 여기에 추가 가능
    }
}
