package com.tukorea.sirojungbotong

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.tukorea.sirojungbotong.model.FilterState

class FilterBottomSheetFragment(
    private val initialState: FilterState,
    private val onApply: (FilterState) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var cbUseSiru: CheckBox
    private lateinit var chipGroupMarket: ChipGroup
    private lateinit var chipGroupCategory: ChipGroup

    private val marketNames = listOf("정왕시장", "삼미시장", "도일시장", "오이도전통수산시장")
    private val categoryNames = listOf("농산물", "축산물", "수산물", "가공식품", "의류/신발", "가정용품", "음식점", "기타소매업", "근린생활서비스")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.filter_bottom_sheet, container, false)

        cbUseSiru = view.findViewById(R.id.cb_use_siru)
        chipGroupMarket = view.findViewById(R.id.chip_group_market)
        chipGroupCategory = view.findViewById(R.id.chip_group_category)

        cbUseSiru.isChecked = initialState.useSiru

        // ✅ 지역 칩 생성
        marketNames.forEach { name ->
            val chip = Chip(requireContext()).apply {
                text = name
                isCheckable = true
                isChecked = initialState.selectedMarkets.contains(name)

                chipCornerRadius = 999f
                setTextColor(ContextCompat.getColorStateList(context, R.color.chip_text_color))
                chipBackgroundColor = ContextCompat.getColorStateList(context, android.R.color.white)
                chipStrokeWidth = 2f
                chipStrokeColor = ContextCompat.getColorStateList(context, R.color.chip_stroke_color)
            }
            chipGroupMarket.addView(chip)
        }

        // ✅ 카테고리 칩 생성
        categoryNames.forEach { name ->
            val chip = Chip(requireContext()).apply {
                text = name
                isCheckable = true
                isChecked = initialState.selectedCategories.contains(name)

                chipCornerRadius = 999f
                setTextColor(ContextCompat.getColorStateList(context, R.color.chip_text_color))
                chipBackgroundColor = ContextCompat.getColorStateList(context, android.R.color.white)
                chipStrokeWidth = 2f
                chipStrokeColor = ContextCompat.getColorStateList(context, R.color.chip_stroke_color)
            }
            chipGroupCategory.addView(chip)
        }

        return view
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val selectedMarkets = mutableSetOf<String>()
        val selectedCategories = mutableSetOf<String>()

        for (i in 0 until chipGroupMarket.childCount) {
            val chip = chipGroupMarket.getChildAt(i) as Chip
            if (chip.isChecked) selectedMarkets.add(chip.text.toString())
        }

        for (i in 0 until chipGroupCategory.childCount) {
            val chip = chipGroupCategory.getChildAt(i) as Chip
            if (chip.isChecked) selectedCategories.add(chip.text.toString())
        }

        val newState = FilterState(
            useSiru = cbUseSiru.isChecked,
            selectedMarkets = selectedMarkets,
            selectedCategories = selectedCategories
        )

        onApply(newState)
    }
}
