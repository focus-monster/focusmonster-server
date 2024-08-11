package com.focusmonster.controller

import com.focusmonster.document.user.User
import com.focusmonster.service.user.UserSignUpService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AnonymousUserController(
    private val userSignUpService: UserSignUpService
) {
    @PostMapping("/users/signUpAnonymous")
    fun createAnonymousUser(): ResponseEntity<User> {
        return ResponseEntity.ok(userSignUpService.signUpAnonymousUser())
    }
}