package com.jayoungup.sirojungbotong.domain.member.exception

import com.jayoungup.sirojungbotong.global.exception.BaseException
import org.springframework.http.HttpStatus

class MemberNotFoundException(id: Long) :
    BaseException("멤버 ID [$id]는 존재하지 않습니다.", HttpStatus.NOT_FOUND)