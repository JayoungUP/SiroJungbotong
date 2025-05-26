package com.jayoungup.sirojungbotong.domain.flyer.exception

import com.jayoungup.sirojungbotong.global.exception.BaseException
import org.springframework.http.HttpStatus

class FlyerNotFoundException(id: Long) :
    BaseException("전단지 ID [$id]는 존재하지 않습니다.", HttpStatus.NOT_FOUND)