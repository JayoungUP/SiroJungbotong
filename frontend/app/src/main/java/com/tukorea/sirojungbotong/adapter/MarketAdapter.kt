package com.tukorea.sirojungbotong.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tukorea.sirojungbotong.R
import com.tukorea.sirojungbotong.network.Flyer
import com.tukorea.sirojungbotong.network.MarketData

class MarketAdapter(
    private val marketList: List<MarketData>,
    private val storeNameMap: Map<Int, String>,
    private val onItemClick: (Flyer) -> Unit // 전단지 클릭 이벤트 콜백
) : RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    inner class MarketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMarketName: TextView = view.findViewById(R.id.tv_market_name)
        val rvItems: RecyclerView = view.findViewById(R.id.rv_items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_market_group, parent, false)
        return MarketViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val market = marketList[position]
        holder.tvMarketName.text = market.marketName

        holder.rvItems.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        holder.rvItems.adapter = FlyerAdapter(market.items, storeNameMap) { flyer ->
            // 콜백 전달
            onItemClick(flyer)
        }
    }

    override fun getItemCount(): Int = marketList.size
}