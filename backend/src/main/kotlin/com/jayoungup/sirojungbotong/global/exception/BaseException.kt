package com.jayoungup.sirojungbotong.global.exception

import org.springframework.http.HttpStatus

abstract class BaseException(
    override val message: String,
    val status: HttpStatus
) : RuntimeException(message)