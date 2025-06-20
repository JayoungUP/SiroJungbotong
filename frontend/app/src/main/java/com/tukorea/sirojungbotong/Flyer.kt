package com.tukorea.sirojungbotong.model

data class MarketData(
    val marketName: String,
    val items: List<Flyer>
)

data class Flyer(
    val id: Int,
    val storeId: Int,
    val description: String,
    val imageUrl: String,
    val items: List<FlyerItem>
)

data class FlyerItem(
    val id: Int,
    val flyerId: Int,
    val name: String,
    val price: Int,
    val imageUrl: String
)