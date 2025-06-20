package com.tukorea.sirojungbotong

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tukorea.sirojungbotong.model.Flyer

class FlyerAdapter(private val flyerList: List<Flyer>) :
    RecyclerView.Adapter<FlyerAdapter.FlyerViewHolder>() {

    inner class FlyerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivFlyer: ImageView = view.findViewById(R.id.iv_flyer_image)
        val tvItemName: TextView = view.findViewById(R.id.tv_item_name)
        val tvPrice: TextView = view.findViewById(R.id.tv_price)
        val tvStore: TextView = view.findViewById(R.id.tv_store)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlyerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flyer, parent, false)
        return FlyerViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlyerViewHolder, position: Int) {
        val item = flyerList[position].items.firstOrNull() ?: return

        holder.tvItemName.text = item.name
        holder.tvPrice.text = "₩${item.price}"
        holder.tvStore.text = "상호 ${flyerList[position].storeId}"

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .into(holder.ivFlyer)
    }

    override fun getItemCount(): Int = flyerList.size
}