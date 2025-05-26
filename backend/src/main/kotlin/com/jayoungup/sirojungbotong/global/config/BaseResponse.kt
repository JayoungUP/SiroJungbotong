package com.jayoungup.sirojungbotong.global.config

data class BaseResponse<T> (
    val status: Int,
    val data: T
)