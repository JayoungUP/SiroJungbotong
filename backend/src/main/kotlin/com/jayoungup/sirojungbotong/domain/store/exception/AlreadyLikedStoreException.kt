package com.jayoungup.sirojungbotong.domain.store.exception

import com.jayoungup.sirojungbotong.global.exception.BaseException
import org.springframework.http.HttpStatus

class AlreadyLikedStoreException : BaseException(
    message = "이미 즐겨찾기한 가게입니다.",
    status = HttpStatus.BAD_REQUEST
)