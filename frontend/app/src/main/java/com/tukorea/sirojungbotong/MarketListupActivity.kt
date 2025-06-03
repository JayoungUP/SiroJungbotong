package com.tukorea.sirojungbotong

import android.content.Context
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import com.tukorea.sirojungbotong.databinding.ListUpBinding
import java.util.Locale

class MarketListupActivity : AppCompatActivity() {

    private lateinit var binding: ListUpBinding

    // 현재 표시된 Item 개수 (1에서 시작, 최대 3)
    private var visibleItemCount = 1

    // SharedPreferences 키
    private val PREFS_NAME            = "app_settings"
    private val KEY_SELECTED_LANG     = "selected_language"
    private val KEY_VISIBLE_COUNT     = "VISIBLE_COUNT"        // 꼭 필요합니다.

    private val KEY_ITEM1_NAME        = "item1_name"
    private val KEY_ITEM1_DESC        = "item1_desc"
    private val KEY_ITEM1_PRICE       = "item1_price"
    private val KEY_ITEM1_USENOW      = "item1_usenow"
    private val KEY_ITEM1_START       = "item1_start"
    private val KEY_ITEM1_END         = "item1_end"
    private val KEY_ITEM1_PHOTO_URI   = "item1_photo_uri"

    private val KEY_ITEM2_NAME        = "item2_name"
    private val KEY_ITEM2_DESC        = "item2_desc"
    private val KEY_ITEM2_PRICE       = "item2_price"
    private val KEY_ITEM2_USENOW      = "item2_usenow"
    private val KEY_ITEM2_START       = "item2_start"
    private val KEY_ITEM2_END         = "item2_end"
    private val KEY_ITEM2_PHOTO_URI   = "item2_photo_uri"

    private val KEY_ITEM3_NAME        = "item3_name"
    private val KEY_ITEM3_DESC        = "item3_desc"
    private val KEY_ITEM3_PRICE       = "item3_price"
    private val KEY_ITEM3_USENOW      = "item3_usenow"
    private val KEY_ITEM3_START       = "item3_start"
    private val KEY_ITEM3_END         = "item3_end"
    private val KEY_ITEM3_PHOTO_URI   = "item3_photo_uri"

    private var item1PhotoUri: Uri? = null
    private var item2PhotoUri: Uri? = null
    private var item3PhotoUri: Uri? = null

    // 어느 항목의 사진을 고르는지 구분
    private var currentPhotoTarget: Int = 0

    private val pickPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri == null) return@registerForActivityResult

            when (currentPhotoTarget) {
                1 -> {
                    item1PhotoUri = uri
                    binding.ivItemPhotoPreview1.setImageURI(uri)
                    binding.ivItemPhotoIcon1.visibility = View.GONE
                    binding.tvItemPhotoGuidance1.visibility = View.GONE
                    binding.ivItemPhotoPreview1.visibility = View.VISIBLE
                }
                2 -> {
                    item2PhotoUri = uri
                    binding.ivItemPhotoPreview2.setImageURI(uri)
                    binding.ivItemPhotoIcon2.visibility = View.GONE
                    binding.tvItemPhotoGuidance2.visibility = View.GONE
                    binding.ivItemPhotoPreview2.visibility = View.VISIBLE
                }
                3 -> {
                    item3PhotoUri = uri
                    binding.ivItemPhotoPreview3.setImageURI(uri)
                    binding.ivItemPhotoIcon3.visibility = View.GONE
                    binding.tvItemPhotoGuidance3.visibility = View.GONE
                    binding.ivItemPhotoPreview3.visibility = View.VISIBLE
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            // 최초 만들어질 때만 실행: 이전에 남아 있던 모든 키를 지워 버림
            val freshPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            freshPrefs.edit().clear().apply()}

        // 1) 저장된 언어 코드 불러와서 Locale 적용
        val prefs    = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedLang= prefs.getString(KEY_SELECTED_LANG, "ko") ?: "ko"
        if (Locale.getDefault().language != savedLang) {
            updateAppLocale(savedLang)
        }

        super.onCreate(savedInstanceState)
        binding = ListUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2) Toolbar를 액션바로 설정 및 뒤로가기 아이콘 클릭하면 finish()
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // 3) 뷰 초기화
        setupLanguageButtons()
        updateLanguageSelectorUI(savedLang)  // 초기 언어 선택 상태 반영

        setupItemLabels()
        setupAddItemLogic()
        setupDatePickers()
        setupPhotoPickers()

        restoreAllFields()
        setupRegisterButton()

    }

    private fun setupRegisterButton() {
        binding.btnRegister.setOnClickListener {
            // 클릭 후 중복 방지
            binding.btnRegister.isEnabled = false

            Handler(Looper.getMainLooper()).postDelayed({
                binding.ivRegisterDoneOverlay.visibility = View.VISIBLE

                // 3) 3초 후에 액티비티 종료
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 3000L)

            }, 1500L)
        }
    }

    /** 언어 변경 버튼 클릭 리스너 설정 **/
    private fun setupLanguageButtons() {
        binding.btnLangKo.setOnClickListener {
            setAppLocale("ko")
            updateLanguageSelectorUI("ko")
        }
        binding.btnLangEn.setOnClickListener {
            setAppLocale("en")
            updateLanguageSelectorUI("en")
        }
        binding.btnLangZh.setOnClickListener {
            setAppLocale("zh")
            updateLanguageSelectorUI("zh")
        }
    }

    /**
     * 클릭된 언어 버튼만 활성 색(검정)＋흰 배경,
     * 나머지는 비활성 색(회색)＋회색 배경 으로 변경
     */
    private fun updateLanguageSelectorUI(selectedLang: String) {
        // 텍스트 활성/비활성 색
        val activeTextColor   = ContextCompat.getColor(this, R.color.lang_active_color)
        val inactiveTextColor = ContextCompat.getColor(this, R.color.lang_inactive_color)

        // 배경 활성(흰색) / 비활성(회색)
        val activeBgColor     = ContextCompat.getColor(this, R.color.white)
        val inactiveBgColor   = ContextCompat.getColor(this, R.color.gray01)

        // “한” 버튼
        if (selectedLang == "ko") {
            binding.btnLangKo.setTextColor(activeTextColor)
            binding.btnLangKo.backgroundTintList = ColorStateList.valueOf(activeBgColor)
        } else {
            binding.btnLangKo.setTextColor(inactiveTextColor)
            binding.btnLangKo.backgroundTintList = ColorStateList.valueOf(inactiveBgColor)
        }

        // “En” 버튼
        if (selectedLang == "en") {
            binding.btnLangEn.setTextColor(activeTextColor)
            binding.btnLangEn.backgroundTintList = ColorStateList.valueOf(activeBgColor)
        } else {
            binding.btnLangEn.setTextColor(inactiveTextColor)
            binding.btnLangEn.backgroundTintList = ColorStateList.valueOf(inactiveBgColor)
        }

        // “中” 버튼
        if (selectedLang == "zh") {
            binding.btnLangZh.setTextColor(activeTextColor)
            binding.btnLangZh.backgroundTintList = ColorStateList.valueOf(activeBgColor)
        } else {
            binding.btnLangZh.setTextColor(inactiveTextColor)
            binding.btnLangZh.backgroundTintList = ColorStateList.valueOf(inactiveBgColor)
        }
    }

    /** “+ 품목 추가하기” 버튼 클릭 시 Item 2/3을 순차적으로 보여주기 **/
    private fun setupAddItemLogic() {
        binding.tvAddItem.setOnClickListener {
            when (visibleItemCount) {
                1 -> {
                    binding.layoutItem2Container.visibility = View.VISIBLE
                    visibleItemCount = 2
                }
                2 -> {
                    binding.layoutItem3Container.visibility = View.VISIBLE
                    visibleItemCount = 3
                    binding.tvAddItem.visibility = View.GONE
                }
            }
        }
    }

    /** 각 Item 레이블 초기화 **/
    private fun setupItemLabels() {
        binding.tvItemLabel1.text = getString(R.string.label_item, 1)
        binding.tvItemLabel2.text = getString(R.string.label_item, 2)
        binding.tvItemLabel3.text = getString(R.string.label_item, 3)
    }

    /** DatePickerDialog 띄우기 **/
    private fun setupDatePickers() {
        binding.etStartDate1.setOnClickListener {
            showDatePicker { y, m, d ->
                binding.etStartDate1.setText(String.format("%d/%02d/%02d", y, m + 1, d))
            }
        }
        binding.etEndDate1.setOnClickListener {
            showDatePicker { y, m, d ->
                binding.etEndDate1.setText(String.format("%d/%02d/%02d", y, m + 1, d))
            }
        }
        binding.etStartDate2.setOnClickListener {
            showDatePicker { y, m, d ->
                binding.etStartDate2.setText(String.format("%d/%02d/%02d", y, m + 1, d))
            }
        }
        binding.etEndDate2.setOnClickListener {
            showDatePicker { y, m, d ->
                binding.etEndDate2.setText(String.format("%d/%02d/%02d", y, m + 1, d))
            }
        }
        binding.etStartDate3.setOnClickListener {
            showDatePicker { y, m, d ->
                binding.etStartDate3.setText(String.format("%d/%02d/%02d", y, m + 1, d))
            }
        }
        binding.etEndDate3.setOnClickListener {
            showDatePicker { y, m, d ->
                binding.etEndDate3.setText(String.format("%d/%02d/%02d", y, m + 1, d))
            }
        }
    }

    /** 사진 선택 리스너 **/
    private fun setupPhotoPickers() {
        binding.layoutItemPhoto1.setOnClickListener {
            currentPhotoTarget = 1
            pickPhotoLauncher.launch("image/*")
        }
        binding.layoutItemPhoto2.setOnClickListener {
            currentPhotoTarget = 2
            pickPhotoLauncher.launch("image/*")
        }
        binding.layoutItemPhoto3.setOnClickListener {
            currentPhotoTarget = 3
            pickPhotoLauncher.launch("image/*")
        }
    }

    /** DatePickerDialog 헬퍼 **/
    private fun showDatePicker(onDateSelected: (year: Int, month: Int, day: Int) -> Unit) {
        val now = java.util.Calendar.getInstance()
        val dpd = android.app.DatePickerDialog(
            this,
            { _, y, m, d -> onDateSelected(y, m, d) },
            now.get(java.util.Calendar.YEAR),
            now.get(java.util.Calendar.MONTH),
            now.get(java.util.Calendar.DAY_OF_MONTH)
        )
        dpd.show()
    }

    /** onStop: 입력된 모든 필드 값 + 사진 Uri 저장 **/
    override fun onStop() {
        super.onStop()
        val prefs  = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putInt(KEY_VISIBLE_COUNT, visibleItemCount)

        // --- Item1 ---
        editor.putString(KEY_ITEM1_NAME,   binding.etItemName1.text.toString())
        editor.putString(KEY_ITEM1_DESC,   binding.etItemDesc1.text.toString())
        editor.putString(KEY_ITEM1_PRICE,  binding.etPrice1.text.toString())
        editor.putBoolean(KEY_ITEM1_USENOW, binding.cbUseNow1.isChecked)
        editor.putString(KEY_ITEM1_START,  binding.etStartDate1.text.toString())
        editor.putString(KEY_ITEM1_END,    binding.etEndDate1.text.toString())
        editor.putString(KEY_ITEM1_PHOTO_URI, item1PhotoUri?.toString())

        // --- Item2 ---
        editor.putString(KEY_ITEM2_NAME,   binding.etItemName2.text.toString())
        editor.putString(KEY_ITEM2_DESC,   binding.etItemDesc2.text.toString())
        editor.putString(KEY_ITEM2_PRICE,  binding.etPrice2.text.toString())
        editor.putBoolean(KEY_ITEM2_USENOW, binding.cbUseNow2.isChecked)
        editor.putString(KEY_ITEM2_START,  binding.etStartDate2.text.toString())
        editor.putString(KEY_ITEM2_END,    binding.etEndDate2.text.toString())
        editor.putString(KEY_ITEM2_PHOTO_URI, item2PhotoUri?.toString())

        // --- Item3 ---
        editor.putString(KEY_ITEM3_NAME,   binding.etItemName3.text.toString())
        editor.putString(KEY_ITEM3_DESC,   binding.etItemDesc3.text.toString())
        editor.putString(KEY_ITEM3_PRICE,  binding.etPrice3.text.toString())
        editor.putBoolean(KEY_ITEM3_USENOW, binding.cbUseNow3.isChecked)
        editor.putString(KEY_ITEM3_START,  binding.etStartDate3.text.toString())
        editor.putString(KEY_ITEM3_END,    binding.etEndDate3.text.toString())
        editor.putString(KEY_ITEM3_PHOTO_URI, item3PhotoUri?.toString())

        editor.apply()
    }

    /** SharedPreferences에서 읽어와서 모든 필드 복원 **/
    private fun restoreAllFields() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // 1) visibleItemCount 복원 → Item2/3 컨테이너 가시성 세팅
        visibleItemCount = prefs.getInt(KEY_VISIBLE_COUNT, 1)
        if (visibleItemCount >= 2) {
            binding.layoutItem2Container.visibility = View.VISIBLE
        }
        if (visibleItemCount >= 3) {
            binding.layoutItem3Container.visibility = View.VISIBLE
            binding.tvAddItem.visibility = View.GONE
        }

        // --- Item1 복원 ---
        binding.etItemName1.setText(prefs.getString(KEY_ITEM1_NAME, "") ?: "")
        binding.etItemDesc1.setText(prefs.getString(KEY_ITEM1_DESC, "") ?: "")
        binding.etPrice1.setText(prefs.getString(KEY_ITEM1_PRICE, "") ?: "")
        binding.cbUseNow1.isChecked = prefs.getBoolean(KEY_ITEM1_USENOW, false)
        binding.etStartDate1.setText(prefs.getString(KEY_ITEM1_START, "") ?: "")
        binding.etEndDate1.setText(prefs.getString(KEY_ITEM1_END, "") ?: "")

        prefs.getString(KEY_ITEM1_PHOTO_URI, null)?.let { uriString ->
            val uri = Uri.parse(uriString)
            item1PhotoUri = uri
            binding.ivItemPhotoPreview1.setImageURI(uri)
            binding.ivItemPhotoIcon1.visibility = View.GONE
            binding.tvItemPhotoGuidance1.visibility = View.GONE
            binding.ivItemPhotoPreview1.visibility = View.VISIBLE
        }

        // --- Item2 복원 ---
        binding.etItemName2.setText(prefs.getString(KEY_ITEM2_NAME, "") ?: "")
        binding.etItemDesc2.setText(prefs.getString(KEY_ITEM2_DESC, "") ?: "")
        binding.etPrice2.setText(prefs.getString(KEY_ITEM2_PRICE, "") ?: "")
        binding.cbUseNow2.isChecked = prefs.getBoolean(KEY_ITEM2_USENOW, false)
        binding.etStartDate2.setText(prefs.getString(KEY_ITEM2_START, "") ?: "")
        binding.etEndDate2.setText(prefs.getString(KEY_ITEM2_END, "") ?: "")

        prefs.getString(KEY_ITEM2_PHOTO_URI, null)?.let { uriString ->
            val uri = Uri.parse(uriString)
            item2PhotoUri = uri
            binding.ivItemPhotoPreview2.setImageURI(uri)
            binding.ivItemPhotoIcon2.visibility = View.GONE
            binding.tvItemPhotoGuidance2.visibility = View.GONE
            binding.ivItemPhotoPreview2.visibility = View.VISIBLE
        }

        // --- Item3 복원 ---
        binding.etItemName3.setText(prefs.getString(KEY_ITEM3_NAME, "") ?: "")
        binding.etItemDesc3.setText(prefs.getString(KEY_ITEM3_DESC, "") ?: "")
        binding.etPrice3.setText(prefs.getString(KEY_ITEM3_PRICE, "") ?: "")
        binding.cbUseNow3.isChecked = prefs.getBoolean(KEY_ITEM3_USENOW, false)
        binding.etStartDate3.setText(prefs.getString(KEY_ITEM3_START, "") ?: "")
        binding.etEndDate3.setText(prefs.getString(KEY_ITEM3_END, "") ?: "")

        prefs.getString(KEY_ITEM3_PHOTO_URI, null)?.let { uriString ->
            val uri = Uri.parse(uriString)
            item3PhotoUri = uri
            binding.ivItemPhotoPreview3.setImageURI(uri)
            binding.ivItemPhotoIcon3.visibility = View.GONE
            binding.tvItemPhotoGuidance3.visibility = View.GONE
            binding.ivItemPhotoPreview3.visibility = View.VISIBLE
        }
    }

    /** Context 확장: 리소스 Configuration을 새 Locale로 업데이트 **/
    private fun Context.updateAppLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration.apply {
            setLocale(locale)
        }
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    /** Activity 확장: 언어 코드 저장 → Locale 업데이트 → 화면 재생성 **/
    private fun AppCompatActivity.setAppLocale(languageCode: String) {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_SELECTED_LANG, languageCode)
            .apply()
        this.updateAppLocale(languageCode)
        recreate()
    }
}
