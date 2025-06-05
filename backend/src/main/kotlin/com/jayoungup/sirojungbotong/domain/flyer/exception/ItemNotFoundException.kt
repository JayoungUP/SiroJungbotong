package com.jayoungup.sirojungbotong.domain.flyer.exception

import com.jayoungup.sirojungbotong.global.exception.BaseException
import org.springframework.http.HttpStatus

class ItemNotFoundException(id: Long) :
    BaseException("품목 ID [$id]는 존재하지 않습니다.", HttpStatus.NOT_FOUND)