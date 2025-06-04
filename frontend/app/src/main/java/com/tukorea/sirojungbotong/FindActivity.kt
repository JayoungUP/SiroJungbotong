package com.tukorea.sirojungbotong

import android.os.Bundle
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
        // UI 색상 변경
        binding.btnTabId.setBackgroundResource(R.drawable.tab_background_white)
        binding.btnTabId.setTextColor(getColor(R.color.black))
        binding.btnTabPw.setBackgroundColor(android.graphics.Color.TRANSPARENT)
        binding.btnTabPw.setTextColor(getColor(R.color.gray02))

        // 폼 전환
        binding.formContainer.removeAllViews()
        val idForm = layoutInflater.inflate(R.layout.layout_find_id_form, binding.formContainer, false)
        binding.formContainer.addView(idForm)
    }

    private fun showPwForm() {
        binding.btnTabPw.setBackgroundResource(R.drawable.tab_background_white)
        binding.btnTabPw.setTextColor(getColor(R.color.black))
        binding.btnTabId.setBackgroundColor(android.graphics.Color.TRANSPARENT)
        binding.btnTabId.setTextColor(getColor(R.color.gray02))

        binding.formContainer.removeAllViews()
        val pwForm = layoutInflater.inflate(R.layout.layout_find_pw_form, binding.formContainer, false)
        binding.formContainer.addView(pwForm)
    }
}
