package com.jayoungup.sirojungbotong.domain.store.exception

import com.jayoungup.sirojungbotong.global.exception.BaseException
import org.springframework.http.HttpStatus

class StoreNotFoundException : BaseException(
    "해당 업장을 찾을 수 없습니다.", HttpStatus.NOT_FOUND
)