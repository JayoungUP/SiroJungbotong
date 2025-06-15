package com.jayoungup.sirojungbotong.auth.service

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {
    fun sendVerificationEmail(email: String, code: String) {
        val message: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setTo(email)
        helper.setSubject("시로 정보통 인증번호")
        helper.setText("""
        인증번호는 다음과 같습니다:

        $code

        인증번호는 10분간 유효합니다.
    """.trimIndent(), false)

        mailSender.send(message)
    }

}


