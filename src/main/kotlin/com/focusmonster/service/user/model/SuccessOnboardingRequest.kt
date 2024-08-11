package com.focusmonster.service.user.model

data class SuccessOnboardingRequest(
    val socialId: String,
    val nickname: String,
    val job: String
)