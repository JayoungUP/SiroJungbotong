package com.tukorea.sirojungbotong.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.tukorea.sirojungbotong.R
import com.tukorea.sirojungbotong.model.Flyer
import com.tukorea.sirojungbotong.util.PreferenceUtil

class FlyerAdapter(
    private val flyerList: List<Flyer>,
    private val storeNameMap: Map<Int, String>,
    private val onItemClick: (Flyer) -> Unit // ✅ 클릭 이벤트 콜백 추가
) : RecyclerView.Adapter<FlyerAdapter.FlyerViewHolder>() {

    inner class FlyerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivFlyer: ImageView = view.findViewById(R.id.iv_flyer_image)
        val tvDescription: TextView = view.findViewById(R.id.tv_description)
        val tvExpire: TextView = view.findViewById(R.id.tv_expire)
        val tvStore: TextView = view.findViewById(R.id.tv_store)
        val tvUsesSiro: TextView = view.findViewById(R.id.tv_uses_siro)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlyerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flyer, parent, false)
        return FlyerViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlyerViewHolder, position: Int) {
        val flyer = flyerList[position]

        // 🔸 이미지 URL 가공
        val imageUrl = flyer.imageUrl?.let {
            when {
                it.startsWith("/home/juno/app/backend/uploads") ->
                    it.replace("/home/juno/app/backend/uploads", "http://sirojungbotong.r-e.kr/uploads")
                it.startsWith("backend/uploads") ->
                    "http://sirojungbotong.r-e.kr/$it"
                else -> null
            }
        }

        // 🔸 Glide 이미지 로딩 (with Authorization header)
        val token = PreferenceUtil.getAccessToken(holder.itemView.context)
        if (!imageUrl.isNullOrEmpty() && !token.isNullOrEmpty()) {
            val glideUrl = GlideUrl(
                imageUrl,
                LazyHeaders.Builder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            )
            Glide.with(holder.itemView.context).load(glideUrl).into(holder.ivFlyer)
        }

        // 🔸 텍스트 바인딩
        holder.tvDescription.text = flyer.description ?: "내용 없음"
        holder.tvExpire.text = "~" + flyer.expireAt.take(10)
        holder.tvStore.text = storeNameMap[flyer.storeId] ?: "알 수 없는 가게"
        holder.tvUsesSiro.visibility = if (flyer.usesSiro) View.VISIBLE else View.GONE

<<<<<<< HEAD
        // ✅ 클릭 이벤트 연결
        holder.itemView.setOnClickListener {
            onItemClick(flyer)
=======
        // 시루 사용 가능 여부 텍스트 표시
        if (flyer.usesSiro) {
            holder.tvUsesSiro.visibility = View.VISIBLE
        } else {
            holder.tvUsesSiro.visibility = View.GONE
>>>>>>> cce10a85597ccde49c8ee0130835a15581132986
        }
    }

    override fun getItemCount(): Int = flyerList.size
}
