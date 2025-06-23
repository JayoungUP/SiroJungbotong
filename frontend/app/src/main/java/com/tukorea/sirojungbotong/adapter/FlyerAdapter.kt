package com.tukorea.sirojungbotong.adapter

import android.util.Log
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
import com.tukorea.sirojungbotong.network.Flyer
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
            val cleanPath = it.replace("backend/", "")
            when {
                cleanPath.startsWith("http") -> cleanPath
                cleanPath.startsWith("/uploads") -> "http://sirojungbotong.r-e.kr$cleanPath"
                cleanPath.startsWith("uploads") -> "http://sirojungbotong.r-e.kr/$cleanPath"
                else -> "http://sirojungbotong.r-e.kr/uploads/${cleanPath.substringAfterLast('/')}"
            }
        }
        Log.d("FlyerAdapter", "imageUrl: $imageUrl")
        // 🔸 Glide 이미지 로딩 (Authorization 있을 때만 붙이고, 없으면 그냥)
        if (!imageUrl.isNullOrEmpty()) {
            val context = holder.itemView.context
            val token = PreferenceUtil.getAccessToken(context)

            if (!token.isNullOrEmpty()) {
                val glideUrl = GlideUrl(
                    imageUrl,
                    LazyHeaders.Builder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                )
                Glide.with(context).load(glideUrl).into(holder.ivFlyer)
            } else {
                Glide.with(context).load(imageUrl).into(holder.ivFlyer)
            }
        }

        // 🔸 텍스트 바인딩
        holder.tvDescription.text = flyer.description ?: "내용 없음"
        holder.tvExpire.text = "~" + flyer.expireAt.take(10)
        holder.tvStore.text = storeNameMap[flyer.storeId.toInt()] ?: "알 수 없는 가게"
        holder.tvUsesSiro.visibility = if (flyer.usesSiro) View.VISIBLE else View.GONE

        // ✅ 클릭 이벤트 연결
        holder.itemView.setOnClickListener {
            onItemClick(flyer)
        }
    }

    override fun getItemCount(): Int = flyerList.size
}
