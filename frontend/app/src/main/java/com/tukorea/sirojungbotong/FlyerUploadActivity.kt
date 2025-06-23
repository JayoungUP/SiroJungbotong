package com.tukorea.sirojungbotong

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tukorea.sirojungbotong.databinding.ActivityFlyerUploadBinding
import com.tukorea.sirojungbotong.network.ApiClient
import com.tukorea.sirojungbotong.network.CreateFlyerResponse
import com.tukorea.sirojungbotong.network.MyStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.Locale

class FlyerUploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFlyerUploadBinding
    private var visibleItemCount = 1
    private var flyerPhotoUri: Uri? = null
    private var item1PhotoUri: Uri? = null
    private var item2PhotoUri: Uri? = null
    private var item3PhotoUri: Uri? = null
    private var currentPhotoTarget: Int = -1
    private var storeList: List<MyStore> = emptyList()
    private var selectedStoreId: Long? = null

    override fun attachBaseContext(newBase: Context) {
        val lang = newBase.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
            .getString("lang", "ko") ?: "ko"
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = newBase.resources.configuration
        config.setLocale(locale)
        val context = newBase.createConfigurationContext(config)
        super.attachBaseContext(context)
    }

    private val pickPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri == null) return@registerForActivityResult
            when (currentPhotoTarget) {
                0 -> showFlyerPhoto(uri)
                1 -> showItemPhoto(1, uri)
                2 -> showItemPhoto(2, uri)
                3 -> showItemPhoto(3, uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlyerUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeLanguage()
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { finish() }

        setupItemLabels()
        setupAddItemLogic()
        setupDatePickers()
        setupPhotoPickers()
        restoreAllFields()
        setupRegisterButton()
        fetchMyStores()
    }

    private fun restoreAllFields() {
        val prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        binding.etFlyerCategory.setText(prefs.getString("flyer_category", "") ?: "")
        binding.etFlyerDescription.setText(prefs.getString("flyer_description", "") ?: "")
        binding.etExpireAt.setText(prefs.getString("flyer_expireAt", "") ?: "")
        binding.cbUsesSiro.isChecked = prefs.getBoolean("flyer_usesSiro", false)
        prefs.getString("flyer_photo_uri", null)?.let {
            showFlyerPhoto(Uri.parse(it))
        }

        visibleItemCount = prefs.getInt("VISIBLE_COUNT", 1)
        if (visibleItemCount >= 2) binding.layoutItem2Container.visibility = View.VISIBLE
        if (visibleItemCount >= 3) {
            binding.layoutItem3Container.visibility = View.VISIBLE
            binding.tvAddItem.visibility = View.GONE
        }

        binding.etItemName1.setText(prefs.getString("item1_name", "") ?: "")
        binding.etItemDesc1.setText(prefs.getString("item1_desc", "") ?: "")
        binding.etPrice1.setText(prefs.getString("item1_price", "") ?: "")
        binding.etStartDate1.setText(prefs.getString("item1_start", "") ?: "")
        binding.etEndDate1.setText(prefs.getString("item1_end", "") ?: "")
        prefs.getString("item1_photo_uri", null)?.let {
            showItemPhoto(1, Uri.parse(it))
        }

        binding.etItemName2.setText(prefs.getString("item2_name", "") ?: "")
        binding.etItemDesc2.setText(prefs.getString("item2_desc", "") ?: "")
        binding.etPrice2.setText(prefs.getString("item2_price", "") ?: "")
        binding.etStartDate2.setText(prefs.getString("item2_start", "") ?: "")
        binding.etEndDate2.setText(prefs.getString("item2_end", "") ?: "")
        prefs.getString("item2_photo_uri", null)?.let {
            showItemPhoto(2, Uri.parse(it))
        }

        binding.etItemName3.setText(prefs.getString("item3_name", "") ?: "")
        binding.etItemDesc3.setText(prefs.getString("item3_desc", "") ?: "")
        binding.etPrice3.setText(prefs.getString("item3_price", "") ?: "")
        binding.etStartDate3.setText(prefs.getString("item3_start", "") ?: "")
        binding.etEndDate3.setText(prefs.getString("item3_end", "") ?: "")
        prefs.getString("item3_photo_uri", null)?.let {
            showItemPhoto(3, Uri.parse(it))
        }
    }

    private fun setupItemLabels() {
        binding.tvItemLabel1.text = getString(R.string.label_item, 1)
        binding.tvItemLabel2.text = getString(R.string.label_item, 2)
        binding.tvItemLabel3.text = getString(R.string.label_item, 3)
    }

    private fun fetchMyStores() {
        val api = ApiClient.create(applicationContext)
        lifecycleScope.launch {
            try {
                val res = api.getMyStores()
                if (res.isSuccessful && res.body()?.status == 200) {
                    storeList = res.body()?.data ?: emptyList()
                    val storeNames = storeList.map { "${it.name} (${it.market})" }
                    val adapter = ArrayAdapter(this@FlyerUploadActivity, android.R.layout.simple_spinner_item, storeNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerStores.adapter = adapter
                    binding.spinnerStores.visibility = View.VISIBLE

                    binding.spinnerStores.setSelection(0)
                    selectedStoreId = storeList.firstOrNull()?.id

                    binding.spinnerStores.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long) {
                            selectedStoreId = storeList[position].id
                        }

                        override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
                    })
                } else {
                    Toast.makeText(this@FlyerUploadActivity, "매장 목록 로딩 실패", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@FlyerUploadActivity, "매장 목록 요청 오류", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRegisterButton() {
        binding.btnRegister.setOnClickListener {
            binding.btnRegister.isEnabled = false

            val storeId = selectedStoreId?.toString() ?: ""
            val flyerCat = binding.etFlyerCategory.text.toString()
            val flyerDesc = binding.etFlyerDescription.text.toString()
            val expireAt = binding.etExpireAt.text.toString()
            val usesSiro = binding.cbUsesSiro.isChecked.toString()

            val items = collectVisibleItems()
            val first = items.first()
            if (first.name.isBlank() || first.category.isBlank() || first.price <= 0 || first.validFrom.isBlank() || first.validUntil.isBlank()) {
                Toast.makeText(this, "첫 번째 품목의 이름·카테고리·가격·유효기간을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.btnRegister.isEnabled = true
                return@setOnClickListener
            }

            val api = ApiClient.create(applicationContext)
            lifecycleScope.launch {
                try {
                    val flyerImagePart = flyerPhotoUri?.let { makeImagePart(it, "flyer.jpg") }
                    val createResp = api.createFlyer(
                        storeId = storeId.toRequestBody("text/plain".toMediaType()),
                        category = flyerCat.toRequestBody("text/plain".toMediaType()),
                        description = flyerDesc.toRequestBody("text/plain".toMediaType()),
                        expireAt = expireAt.toRequestBody("text/plain".toMediaType()),
                        usesSiro = usesSiro.toRequestBody("text/plain".toMediaType()),
                        image = flyerImagePart
                    ).body() as CreateFlyerResponse

                    val flyerIdNew = createResp.id
                    items.forEachIndexed { idx, item ->
                        val itemImage = item.photoUri?.let { makeImagePart(it, "item${idx + 1}.jpg") }
                        api.addItem(
                            flyerId = flyerIdNew,
                            name = item.name.toRequestBody("text/plain".toMediaType()),
                            price = item.price.toString().toRequestBody("text/plain".toMediaType()),
                            validFrom = item.validFrom.toRequestBody("text/plain".toMediaType()),
                            validUntil = item.validUntil.toRequestBody("text/plain".toMediaType()),
                            image = itemImage
                        )
                    }

                    binding.ivRegisterDoneOverlay.visibility = View.VISIBLE
                    delay(3000L)
                    finish()
                } catch (e: Exception) {
                    binding.btnRegister.isEnabled = true
                    Toast.makeText(this@FlyerUploadActivity, "등록 중 오류 발생: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun makeImagePart(uri: Uri, filename: String): MultipartBody.Part? {
        return contentResolver.openInputStream(uri)?.use { stream ->
            val bytes = stream.readBytes()
            val rb = bytes.toRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("image", filename, rb)
        }
    }

    private data class UiItem(
        val name: String,
        val category: String,
        val price: Int,
        val validFrom: String,
        val validUntil: String,
        val photoUri: Uri?
    )

    private fun collectVisibleItems(): List<UiItem> {
        val list = mutableListOf<UiItem>()
        list += UiItem(
            binding.etItemName1.text.toString(),
            binding.etItemDesc1.text.toString(),
            binding.etPrice1.text.toString().toIntOrNull() ?: 0,
            binding.etStartDate1.text.toString(),
            binding.etEndDate1.text.toString(),
            item1PhotoUri
        )
        if (visibleItemCount >= 2) {
            list += UiItem(
                binding.etItemName2.text.toString(),
                binding.etItemDesc2.text.toString(),
                binding.etPrice2.text.toString().toIntOrNull() ?: 0,
                binding.etStartDate2.text.toString(),
                binding.etEndDate2.text.toString(),
                item2PhotoUri
            )
        }
        if (visibleItemCount >= 3) {
            list += UiItem(
                binding.etItemName3.text.toString(),
                binding.etItemDesc3.text.toString(),
                binding.etPrice3.text.toString().toIntOrNull() ?: 0,
                binding.etStartDate3.text.toString(),
                binding.etEndDate3.text.toString(),
                item3PhotoUri
            )
        }
        return list
    }

    private fun showFlyerPhoto(uri: Uri) {
        flyerPhotoUri = uri
        binding.ivFlyerImagePreview.setImageURI(uri)
        binding.ivFlyerImagePreview.visibility = View.VISIBLE
        binding.ivFlyerImageIcon.visibility = View.GONE
        binding.tvFlyerImageGuidance.visibility = View.GONE
    }

    private fun showItemPhoto(target: Int, uri: Uri) {
        when (target) {
            1 -> item1PhotoUri = uri
            2 -> item2PhotoUri = uri
            3 -> item3PhotoUri = uri
        }
        val ivPreview = when (target) {
            1 -> binding.ivItemPhotoPreview1
            2 -> binding.ivItemPhotoPreview2
            else -> binding.ivItemPhotoPreview3
        }
        val ivIcon = when (target) {
            1 -> binding.ivItemPhotoIcon1
            2 -> binding.ivItemPhotoIcon2
            else -> binding.ivItemPhotoIcon3
        }
        val tvGuidance = when (target) {
            1 -> binding.tvItemPhotoGuidance1
            2 -> binding.tvItemPhotoGuidance2
            else -> binding.tvItemPhotoGuidance3
        }
        ivPreview.setImageURI(uri)
        ivPreview.visibility = View.VISIBLE
        ivIcon.visibility = View.GONE
        tvGuidance.visibility = View.GONE
    }

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

    private fun setupDatePickers() {
        val fields = listOf(
            binding.etExpireAt to { y: Int, m: Int, d: Int -> "%d/%02d/%02d".format(y, m + 1, d) },
            binding.etStartDate1 to { y: Int, m: Int, d: Int -> "%d/%02d/%02d".format(y, m + 1, d) },
            binding.etEndDate1 to { y: Int, m: Int, d: Int -> "%d/%02d/%02d".format(y, m + 1, d) },
            binding.etStartDate2 to { y: Int, m: Int, d: Int -> "%d/%02d/%02d".format(y, m + 1, d) },
            binding.etEndDate2 to { y: Int, m: Int, d: Int -> "%d/%02d/%02d".format(y, m + 1, d) },
            binding.etStartDate3 to { y: Int, m: Int, d: Int -> "%d/%02d/%02d".format(y, m + 1, d) },
            binding.etEndDate3 to { y: Int, m: Int, d: Int -> "%d/%02d/%02d".format(y, m + 1, d) }
        )
        for ((field, formatter) in fields) {
            field.setOnClickListener {
                val now = java.util.Calendar.getInstance()
                android.app.DatePickerDialog(
                    this,
                    { _, y, m, d -> field.setText(formatter(y, m, d)) },
                    now.get(java.util.Calendar.YEAR),
                    now.get(java.util.Calendar.MONTH),
                    now.get(java.util.Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }

    private fun setupPhotoPickers() {
        binding.layoutFlyerImage.setOnClickListener {
            currentPhotoTarget = 0
            pickPhotoLauncher.launch("image/*")
        }
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

    private fun initializeLanguage() {
        setupLanguageButtons()
        updateLanguageSelectorUI(getCurrentLang())
    }

    private fun setupLanguageButtons() {
        binding.btnLangKo.setOnClickListener {
            setAppLocale("ko")
            restartActivity()
        }
        binding.btnLangEn.setOnClickListener {
            setAppLocale("en")
            restartActivity()
        }
        binding.btnLangZh.setOnClickListener {
            setAppLocale("zh")
            restartActivity()
        }
    }

    private fun restartActivity() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    private fun updateLanguageSelectorUI(lang: String) {
        binding.btnLangKo.alpha = if (lang == "ko") 1f else 0.5f
        binding.btnLangEn.alpha = if (lang == "en") 1f else 0.5f
        binding.btnLangZh.alpha = if (lang == "zh") 1f else 0.5f
    }

    private fun getCurrentLang(): String {
        return resources.configuration.locales[0].language
    }

    private fun Context.updateAppLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun setAppLocale(lang: String) {
        applicationContext.updateAppLocale(lang)
        getSharedPreferences("app_settings", MODE_PRIVATE)
            .edit()
            .putString("lang", lang)
            .apply()
    }
}