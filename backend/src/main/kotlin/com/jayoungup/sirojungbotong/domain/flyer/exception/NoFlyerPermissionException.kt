package com.jayoungup.sirojungbotong.domain.flyer.exception

import com.jayoungup.sirojungbotong.global.exception.BaseException
import org.springframework.http.HttpStatus

class NoFlyerPermissionException : BaseException(
    "해당 전단지에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN
)