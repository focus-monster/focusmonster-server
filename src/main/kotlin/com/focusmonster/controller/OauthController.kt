package com.focusmonster.controller

import com.focusmonster.service.user.UserSignUpService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class OauthController(
    private val userSignUpService: UserSignUpService
) {
    @Value("\${oauth.redirect-uri}")
    lateinit var redirectURI: String

    @GetMapping("/oauth/google")
    fun googleCallback(code: String): String {
        val signUpUser = userSignUpService.signUpWithGoogle(code)
        val socialId = signUpUser.socialId!!

        return "redirect:$redirectURI?socialId=$socialId"
    }
}