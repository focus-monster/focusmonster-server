package com.focusmonster.service.user.model

data class GoogleTokenRequest(
    val code: String,
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String,
    val grantType: String
)