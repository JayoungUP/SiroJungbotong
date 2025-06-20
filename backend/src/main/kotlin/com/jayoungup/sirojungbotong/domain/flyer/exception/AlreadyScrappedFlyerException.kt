package com.jayoungup.sirojungbotong.domain.flyer.exception

import com.jayoungup.sirojungbotong.global.exception.BaseException
import org.springframework.http.HttpStatus

class AlreadyScrappedFlyerException : BaseException(
    message = "이미 스크랩한 전단지입니다.",
    status = HttpStatus.BAD_REQUEST
)