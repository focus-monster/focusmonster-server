package com.focusmonster.service.user.model

data class UpdateUserRequest(
    val socialId: String,
    val nickname: String?,
    val job: String?
)