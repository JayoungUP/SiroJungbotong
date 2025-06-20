package com.tukorea.sirojungbotong

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tukorea.sirojungbotong.model.SortType

class SortBottomSheetFragment(
    private val current: SortType,
    private val onSortSelected: (SortType) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.sort_bottom_sheet, container, false)

        val itemLatest = view.findViewById<LinearLayout>(R.id.item_latest)
        val itemPopular = view.findViewById<LinearLayout>(R.id.item_popular)
        val tvLatest = view.findViewById<TextView>(R.id.tv_latest)
        val tvPopular = view.findViewById<TextView>(R.id.tv_popular)
        val iconLatest = view.findViewById<ImageView>(R.id.icon_latest)
        val iconPopular = view.findViewById<ImageView>(R.id.icon_popular)

        fun updateSelection(type: SortType) {
            val highlightColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            val defaultColor = ContextCompat.getColor(requireContext(), R.color.black)

            tvLatest.setTextColor(if (type == SortType.LATEST) highlightColor else defaultColor)
            tvPopular.setTextColor(if (type == SortType.POPULAR) highlightColor else defaultColor)
            iconLatest.visibility = if (type == SortType.LATEST) View.VISIBLE else View.GONE
            iconPopular.visibility = if (type == SortType.POPULAR) View.VISIBLE else View.GONE
        }

        updateSelection(current)

        itemLatest.setOnClickListener {
            onSortSelected(SortType.LATEST)
            dismiss()
        }

        itemPopular.setOnClickListener {
            onSortSelected(SortType.POPULAR)
            dismiss()
        }

        return view
    }
}
