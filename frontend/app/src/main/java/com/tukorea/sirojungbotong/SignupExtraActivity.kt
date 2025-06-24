package com.example.yourapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tukorea.sirojungbotong.MarketListActivity
import com.david.siro.MarketUploadActivity
import com.tukorea.sirojungbotong.HomeActivity
import com.tukorea.sirojungbotong.R
import com.tukorea.sirojungbotong.databinding.SignupExtraBinding
import com.tukorea.sirojungbotong.network.ApiClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.util.*

class SignupExtraActivity : AppCompatActivity() {
    private lateinit var bind: SignupExtraBinding
    private var selectedImageUri: Uri? = null

    private val pickStorePhoto =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                bind.ivStorePhotoPreview.setImageURI(it)
                bind.ivStorePhotoIcon.visibility = View.GONE
                bind.tvStorePhotoGuidance.visibility = View.GONE
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = SignupExtraBinding.inflate(layoutInflater)
        setContentView(bind.root)

        // 업장 사진 선택
        bind.layoutStorePhoto.setOnClickListener {
            pickStorePhoto.launch("image/*")
        }
        bind.gotoback.setOnClickListener {
            finish()
        }

        // Spinner 어댑터 세팅 (market 과 category)
        ArrayAdapter.createFromResource(
            this, R.array.market_types, android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bind.marketBar.adapter = adapter     // ← XML ID와 일치시켜 줍니다.
        }
        ArrayAdapter.createFromResource(
            this, R.array.business_types, android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bind.spinnerBusinessType.adapter = it
        }
        ArrayAdapter.createFromResource(
            this, R.array.business_types, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bind.spinnerBusinessType.adapter = adapter
        }

        // TimePicker
        bind.etOpenTime.setOnClickListener { showTimePicker(true) }
        bind.etCloseTime.setOnClickListener { showTimePicker(false) }

        bind.btnComplete.setOnClickListener {
            // 1) 입력값 검증
            val name      = bind.etStoreName.text.toString().trim()
            val market    = bind.marketBar.selectedItem.toString()
            val address   = bind.etStoreAddress.text.toString().trim()
            val openTime  = bind.etOpenTime.text.toString().trim()
            val closeTime = bind.etCloseTime.text.toString().trim()
            val category  = bind.spinnerBusinessType.selectedItem.toString()

            if (name.isEmpty() || address.isEmpty() ||
                openTime.isEmpty() || closeTime.isEmpty()) {
                Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val uri = selectedImageUri
            if (uri == null) {
                Toast.makeText(this, "업장 사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2) Coroutine 으로 업로드
            lifecycleScope.launch {
                    // 텍스트 → RequestBody extension
                    fun String.toPart() =
                        toRequestBody("text/plain".toMediaType())

                    // Uri → MultipartBody.Part
                    val stream = contentResolver.openInputStream(uri)!!
                    val tmpFile = File(cacheDir, "upload.jpg").apply {
                        outputStream().use { out -> stream.copyTo(out) }
                    }
                    val req = tmpFile.asRequestBody("image/jpeg".toMediaType())
                    val partImage = MultipartBody.Part.createFormData(
                        "image", tmpFile.name, req
                    )

                    // API 호출
                    val api = ApiClient.create(this@SignupExtraActivity)
                    val resp = api.uploadStore(
                        name.toPart(),
                        market.toPart(),
                        address.toPart(),
                        openTime.toPart(),
                        closeTime.toPart(),
                        category.toPart(),
                        partImage
                    )


                    Toast.makeText(
                        this@SignupExtraActivity,
                        "등록 성공!", Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@SignupExtraActivity, HomeActivity::class.java))
                    finish()
            }
    }}

    private fun showTimePicker(isStart: Boolean) {
        val now = Calendar.getInstance()
        TimePickerDialog(this, { _, h, m ->
            val text = "%02d:%02d".format(h, m)
            if (isStart) bind.etOpenTime.setText(text)
            else        bind.etCloseTime.setText(text)
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true)
            .show()
    }
}

