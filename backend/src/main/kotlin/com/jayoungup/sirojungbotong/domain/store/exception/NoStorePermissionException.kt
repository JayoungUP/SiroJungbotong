package com.jayoungup.sirojungbotong.domain.store.exception

import com.jayoungup.sirojungbotong.global.exception.BaseException
import org.springframework.http.HttpStatus

class NoStorePermissionException : BaseException(
    "본인의 업장만 수정/삭제할 수 있습니다.", HttpStatus.FORBIDDEN
)