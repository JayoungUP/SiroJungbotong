package com.example.yourapp

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
import com.tukorea.sirojungbotong.MarketListActivity
import com.david.siro.MarketUploadActivity
import com.tukorea.sirojungbotong.R
import com.tukorea.sirojungbotong.databinding.SignupExtraBinding
import java.io.IOException
import java.util.*

class SignupExtraActivity : AppCompatActivity() {

    private lateinit var bind: SignupExtraBinding
    private val calendar = Calendar.getInstance()

    // 갤러리에서 업장 사진 선택
    private val pickStorePhoto =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                try {
                    val bitmap: Bitmap =
                        MediaStore.Images.Media.getBitmap(contentResolver, it)
                    bind.ivStorePhotoPreview.setImageBitmap(bitmap)
                    bind.ivStorePhotoPreview.visibility = View.VISIBLE
                    bind.ivStorePhotoIcon.visibility = View.GONE
                    bind.tvStorePhotoGuidance.visibility = View.GONE
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    // 갤러리에서 사업자 등록 서류 선택
    private val pickBusinessDoc =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                try {
                    val bitmap: Bitmap =
                        MediaStore.Images.Media.getBitmap(contentResolver, it)
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

        // 운영 시작 시간 클릭
        bind.etOpenTime.setOnClickListener {
            showSpinnerTimePicker(isStart = true)
        }

        // 운영 종료 시간 클릭
        bind.etCloseTime.setOnClickListener {
            showSpinnerTimePicker(isStart = false)
        }

        // 업장 사진 영역 클릭 → 갤러리 열기
        bind.layoutStorePhoto.setOnClickListener {
            pickStorePhoto.launch("image/*")
        }

        // 사업자 등록 서류 영역 클릭 → 갤러리 열기
        bind.layoutBusinessDoc.setOnClickListener {
            pickBusinessDoc.launch("image/*")
        }

        // 스위치 토글 시 폼 보이기/숨기기
        bind.switchRegister.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                bind.StoreName.visibility = View.VISIBLE
                bind.tilStoreName.visibility = View.VISIBLE
                bind.etStoreName.visibility = View.VISIBLE

                bind.StoreAddressLabel.visibility = View.VISIBLE
                bind.tilStoreAddress.visibility = View.VISIBLE
                bind.etStoreAddress.visibility = View.VISIBLE

                bind.tvBusinessTypeLabel.visibility = View.VISIBLE
                bind.spinnerBusinessType.visibility = View.VISIBLE

                bind.tilOpenTime.visibility = View.VISIBLE
                bind.etOpenTime.visibility = View.VISIBLE
                bind.tilCloseTime.visibility = View.VISIBLE
                bind.etCloseTime.visibility = View.VISIBLE

                bind.nothing1.visibility = View.VISIBLE
                bind.layoutOperatingHours.visibility = View.VISIBLE
                bind.tvTimeSeparator.visibility = View.VISIBLE

                bind.tvLabelStorePhoto.visibility = View.VISIBLE
                bind.layoutStorePhoto.visibility = View.VISIBLE
                bind.ivStorePhotoIcon.visibility = View.VISIBLE
                bind.tvStorePhotoGuidance.visibility = View.VISIBLE

                bind.tvLabelBusinessDoc.visibility = View.VISIBLE
                bind.layoutBusinessDoc.visibility = View.VISIBLE
                bind.ivBusinessDocIcon.visibility = View.VISIBLE
                bind.tvBusinessDocGuidance.visibility = View.VISIBLE

                bind.btnComplete.text = "가입 완료하기"
            } else {
                bind.StoreName.visibility = View.GONE
                bind.tilStoreName.visibility = View.GONE
                bind.etStoreName.visibility = View.GONE

                bind.StoreAddressLabel.visibility = View.GONE
                bind.tilStoreAddress.visibility = View.GONE
                bind.etStoreAddress.visibility = View.GONE

                bind.nothing1.visibility = View.GONE
                bind.layoutOperatingHours.visibility = View.GONE
                bind.tvTimeSeparator.visibility = View.GONE

                bind.tilOpenTime.visibility = View.GONE
                bind.etOpenTime.visibility = View.GONE
                bind.tilCloseTime.visibility = View.GONE
                bind.etCloseTime.visibility = View.GONE

                bind.tvLabelStorePhoto.visibility = View.GONE
                bind.layoutStorePhoto.visibility = View.GONE
                bind.ivStorePhotoIcon.visibility = View.GONE
                bind.tvStorePhotoGuidance.visibility = View.GONE

                bind.tvLabelBusinessDoc.visibility = View.GONE
                bind.layoutBusinessDoc.visibility = View.GONE
                bind.ivBusinessDocIcon.visibility = View.GONE
                bind.tvBusinessDocGuidance.visibility = View.GONE

                bind.tvBusinessTypeLabel.visibility = View.GONE
                bind.spinnerBusinessType.visibility = View.GONE

                bind.btnComplete.text = "다음으로"
            }

            // Spinner 어댑터 세팅
            ArrayAdapter.createFromResource(
                this,
                R.array.business_types,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                bind.spinnerBusinessType.adapter = adapter
            }

            bind.spinnerBusinessType.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>, view: View?, position: Int, id: Long
                    ) {
                        val selected = parent.getItemAtPosition(position) as String
                        // TODO: 선택된 업종 처리
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }
        }

        // 가입 완료 버튼 클릭
        bind.btnComplete.setOnClickListener {
            if (bind.switchRegister.isChecked) {
                val storeName = bind.etStoreName.text.toString().trim()
                val storeAddress = bind.etStoreAddress.text.toString().trim()
                val openTime = bind.etOpenTime.text.toString().trim()
                val closeTime = bind.etCloseTime.text.toString().trim()

                if (storeName.isEmpty() ||
                    storeAddress.isEmpty() ||
                    openTime.isEmpty() ||
                    closeTime.isEmpty()
                ) {
                    Toast.makeText(this, "업장 정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                Toast.makeText(this, "가입이 완료되었습니다!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MarketUploadActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "업장 등록 없이 다음 단계로 넘어갑니다.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MarketListActivity::class.java))
                finish()
            }
        }
    }

    /**
     * Holo 스피너 모드 TimePickerDialog
     */
    private fun showSpinnerTimePicker(isStart: Boolean) {
        val now = Calendar.getInstance()
        val listener = TimePickerDialog.OnTimeSetListener { _: TimePicker, h, m ->
            val text = String.format("%02d:%02d", h, m)
            if (isStart) bind.etOpenTime.setText(text)
            else        bind.etCloseTime.setText(text)
        }

        val dialog = TimePickerDialog(
            this,
            android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
            listener,
            now.get(Calendar.HOUR_OF_DAY),
            now.get(Calendar.MINUTE),
            /* is24HourView = */ false
        )

        dialog.show()
    }