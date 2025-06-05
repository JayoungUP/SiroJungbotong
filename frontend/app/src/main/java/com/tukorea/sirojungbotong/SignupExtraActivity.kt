package com.example.yourapp

import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.tukorea.sirojungbotong.MarketListActivity
import com.tukorea.sirojungbotong.OnboardActivity
import com.tukorea.sirojungbotong.databinding.SignupExtraBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.IOException
import java.util.*

class SignupExtraActivity : AppCompatActivity() {

    private lateinit var bind: SignupExtraBinding
    private val calendar = Calendar.getInstance()

    // ✅ 일반 사용자 회원가입 API 인터페이스
    interface ApiService {
        @POST("/api/member/signup/user/email")
        fun signupUser(@Body request: SignupRequest): Call<Void>
    }

    data class SignupRequest(
        val loginId: String,
        val email: String,
        val password: String,
        val name: String,
        val nickname: String
    )

    // ✅ Retrofit 객체
    private val retrofit: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://sirojungbotong.r-e.kr")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // ✅ 갤러리 이미지 선택 콜백 (업장 사진)
    private val pickStorePhoto =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                try {
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                    bind.ivStorePhotoPreview.setImageBitmap(bitmap)
                    bind.ivStorePhotoPreview.visibility = View.VISIBLE
                    bind.ivStorePhotoIcon.visibility = View.GONE
                    bind.tvStorePhotoGuidance.visibility = View.GONE
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    // ✅ 갤러리 이미지 선택 콜백 (사업자 서류)
    private val pickBusinessDoc =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                try {
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                    bind.ivBusinessDocPreview.setImageBitmap(bitmap)
                    bind.ivBusinessDocPreview.visibility = View.VISIBLE
                    bind.ivBusinessDocIcon.visibility = View.GONE
                    bind.tvBusinessDocGuidance.visibility = View.GONE
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = SignupExtraBinding.inflate(layoutInflater)
        setContentView(bind.root)

        // 시간 선택 다이얼로그
        bind.etOpenTime.setOnClickListener {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            TimePickerDialog(this, { _: TimePicker, h: Int, m: Int ->
                bind.etOpenTime.setText(String.format("%02d:%02d", h, m))
            }, hour, minute, true).show()
        }

        bind.etCloseTime.setOnClickListener {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            TimePickerDialog(this, { _: TimePicker, h: Int, m: Int ->
                bind.etCloseTime.setText(String.format("%02d:%02d", h, m))
            }, hour, minute, true).show()
        }

        bind.layoutStorePhoto.setOnClickListener {
            pickStorePhoto.launch("image/*")
        }

        bind.layoutBusinessDoc.setOnClickListener {
            pickBusinessDoc.launch("image/*")
        }

        // 스위치 → 단순 UI 제어, API 호출은 일반 사용자로만 진행
        bind.switchRegister.setOnCheckedChangeListener { _, isChecked ->
            val visibility = if (isChecked) View.VISIBLE else View.GONE

            bind.StoreName.visibility = visibility
            bind.tilStoreName.visibility = visibility
            bind.etStoreName.visibility = visibility

            bind.StoreAddressLabel.visibility = visibility
            bind.tilStoreAddress.visibility = visibility
            bind.etStoreAddress.visibility = visibility

            bind.nothing1.visibility = visibility
            bind.layoutOperatingHours.visibility = visibility
            bind.etOpenTime.visibility = visibility
            bind.etCloseTime.visibility = visibility
            bind.tvTimeSeparator.visibility = visibility
            bind.tilOpenTime.visibility = visibility
            bind.tilCloseTime.visibility = visibility

            bind.tvLabelStorePhoto.visibility = visibility
            bind.layoutStorePhoto.visibility = visibility
            bind.ivStorePhotoIcon.visibility = visibility
            bind.tvStorePhotoGuidance.visibility = visibility

            bind.tvLabelBusinessDoc.visibility = visibility
            bind.layoutBusinessDoc.visibility = visibility
            bind.ivBusinessDocIcon.visibility = visibility
            bind.tvBusinessDocGuidance.visibility = visibility

            bind.btnComplete.text = "가입 완료하기"  // 항상 고정
        }

        // ✅ 가입 완료 버튼 클릭
        bind.btnComplete.setOnClickListener {
            val loginId = intent.getStringExtra("loginId") ?: ""
            val email = intent.getStringExtra("email") ?: ""
            val password = intent.getStringExtra("password") ?: ""
            val name = intent.getStringExtra("name") ?: ""
            val nickname = intent.getStringExtra("nickname") ?: ""

            if (loginId.isEmpty() || email.isEmpty() || password.isEmpty() || name.isEmpty() || nickname.isEmpty()) {
                Toast.makeText(this, "회원가입 정보가 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ 사장님 폼이 열려 있다면 유효성 검사만 수행 (API는 호출하지 않음)
            if (bind.switchRegister.isChecked) {
                val storeName = bind.etStoreName.text.toString().trim()
                val storeAddress = bind.etStoreAddress.text.toString().trim()
                val openTime = bind.etOpenTime.text.toString().trim()
                val closeTime = bind.etCloseTime.text.toString().trim()

                if (storeName.isEmpty() || storeAddress.isEmpty() || openTime.isEmpty() || closeTime.isEmpty()) {
                    Toast.makeText(this, "업장 정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // ✅ 실제 API는 항상 일반 사용자용으로 호출
            val request = SignupRequest(loginId, email, password, name, nickname)

            retrofit.signupUser(request).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@SignupExtraActivity, "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SignupExtraActivity, OnboardActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@SignupExtraActivity, "회원가입 실패 (${response.code()})", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@SignupExtraActivity, "네트워크 오류: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
