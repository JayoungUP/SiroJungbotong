package com.tukorea.sirojungbotong

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tukorea.sirojungbotong.model.MarketData

class MarketAdapter(private val marketList: List<MarketData>) :
    RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

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
        holder.rvItems.adapter = FlyerAdapter(market.items)
    }

    override fun getItemCount(): Int = marketList.size
}
