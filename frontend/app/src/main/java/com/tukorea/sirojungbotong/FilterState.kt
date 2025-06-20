package com.tukorea.sirojungbotong.model

data class FilterState(
    val useSiru: Boolean = false,
    val selectedMarkets: Set<String> = emptySet(),
    val selectedCategories: Set<String> = emptySet()
)