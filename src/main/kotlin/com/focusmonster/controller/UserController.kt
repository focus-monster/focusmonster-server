package com.focusmonster.controller

import com.focusmonster.document.user.User
import com.focusmonster.service.user.UserService
import com.focusmonster.service.user.UserValidateService
import com.focusmonster.service.user.model.SuccessOnboardingRequest
import com.focusmonster.service.user.model.UpdateUserRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
    private val userValidateService: UserValidateService
) {
    @GetMapping("/users/{socialId}")
    fun findUserBySocialId(@PathVariable socialId: String): ResponseEntity<User> {
        return ResponseEntity.ok(userService.findUserBySocialId(socialId))
    }

    @GetMapping("/users/all")
    fun findUserBySocialId(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userService.findAllUsers())
    }

    @GetMapping("/users/valid")
    fun existsNickname(@RequestParam nickname: String): ResponseEntity<List<String>> {
        return ResponseEntity.ok(userValidateService.checkNickname(nickname))
    }

    @PostMapping("/users/onboarding")
    fun successOnboarding(@RequestBody successOnboardingRequest: SuccessOnboardingRequest): ResponseEntity<User> {
        return ResponseEntity.ok(userService.successOnboarding(successOnboardingRequest))
    }

    @PutMapping("/users")
    fun updateUser(@RequestBody updateUserRequest: UpdateUserRequest): ResponseEntity<User> {
        return ResponseEntity.ok(userService.updateUser(updateUserRequest))
    }
}