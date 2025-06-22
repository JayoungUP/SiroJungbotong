package com.tukorea.sirojungbotong

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tukorea.sirojungbotong.databinding.SingupEmailBinding
import com.tukorea.sirojungbotong.network.ApiClient
import com.tukorea.sirojungbotong.network.OwnerSignupRequest
import com.tukorea.sirojungbotong.network.UserSignupRequest
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*

class SignupEmailActivity : AppCompatActivity() {
    private lateinit var binding: SingupEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SingupEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.inputdate.setOnClickListener { openDatePicker() }
        binding.opendatebar.setEndIconOnClickListener { openDatePicker() }

        binding.switchRegister.setOnCheckedChangeListener { _, isChecked ->
            Log.d("SIGNUP", "isOwner = ${binding.switchRegister.isChecked}")
            val visibility = if (isChecked) View.VISIBLE else View.GONE
            listOf(
                binding.businesbar, binding.businesnumber, binding.inputbusiness,
                binding.KoreanOwner, binding.KoreanOwnerBar, binding.inputKOwner,
                binding.ForeginerOwner, binding.ForeginerOwnerBar, binding.inputFOwner,
                binding.opendate, binding.opendatebar, binding.inputdate
            ).forEach { it.visibility = visibility }
        }

        binding.btnRegister.setOnClickListener {
            // 1) 공통 입력값 수집
            val name            = binding.etName.text.toString().trim()
            val nickname        = binding.etNickname.text.toString().trim()
            val loginId         = binding.etId.text.toString().trim()
            val password        = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            val email           = binding.etEmail.text.toString().trim()

            // 2) 유효성 검사
            if (listOf(name, nickname, loginId, password, confirmPassword, email).any { it.isEmpty() }) {
                Toast.makeText(this, "모든 필수 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3) ApiClient 로 서비스 얻고, fetch 호출
            val api = ApiClient.create(this)
            lifecycleScope.launch {
                if (binding.switchRegister.isChecked) {
                    // — 점주 회원가입 처리 —
                    val bNo     = binding.inputbusiness.text.toString().trim()
                    val pNm     = binding.inputKOwner.text.toString().trim()
                    val pNm2Raw = binding.inputFOwner.text.toString().trim()
                    val pNm2    = pNm2Raw.takeIf { it.isNotEmpty() }
                    val rawDate = binding.inputdate.text.toString().trim()
                    val startDt = rawDate.replace("/", "-")  // yyyy/MM/dd → yyyy-MM-dd

                    val body = OwnerSignupRequest(
                        loginId  = loginId,
                        email    = email,
                        password = password,
                        name     = name,
                        nickname = nickname,
                        b_no     = bNo,
                        start_dt = startDt,
                        p_nm     = pNm,
                        p_nm2    = pNm2
                    )
                    val resp = api.signupOwner(body)
                    Log.d("OWNER_SIGNUP", "HTTP ${resp.code()}, errorBody=${resp.errorBody()?.string()}")
                    handleResponse(api.signupOwner(body), "점주 회원가입")

                } else {
                    // — 일반 사용자 회원가입 처리 —
                    val body = UserSignupRequest(
                        loginId  = loginId,
                        email    = email,
                        password = password,
                        name     = name,
                        nickname = nickname
                    )
                    handleResponse(api.signupUser(body), "일반 회원가입")
                }
            }
        }
    }

    private fun openDatePicker() {
        val now = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, y, m, d -> binding.inputdate.setText(String.format("%04d/%02d/%02d", y, m + 1, d)) },
            now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun <T> handleResponse(resp: Response<T>, title: String) {
        if (resp.isSuccessful) {
            Toast.makeText(this, "$title 성공!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            val err = resp.errorBody()?.string().orEmpty()
            AlertDialog.Builder(this)
                .setTitle("$title 실패")
                .setMessage(err.ifEmpty { "알 수 없는 오류" })
                .setPositiveButton("확인", null)
                .show()
        }
    }
}
