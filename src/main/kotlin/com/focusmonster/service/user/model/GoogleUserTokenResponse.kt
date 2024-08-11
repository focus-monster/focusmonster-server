package com.focusmonster.service.user.model

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleUserTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("expires_in")
    val expiresIn: Int,
    val scope: String,
    @JsonProperty("token_type")
    val tokenType: String,
    @JsonProperty("id_token")
    val idToken: String
)