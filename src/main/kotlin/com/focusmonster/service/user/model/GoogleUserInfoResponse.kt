package com.focusmonster.service.user.model

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleUserInfoResponse(
    val id: String?,
    val email: String?,
    @JsonProperty("verified_email")
    val verifiedEmail: Boolean?,
    @JsonProperty("given_name")
    val name: String?,
    @JsonProperty("family_name")
    val familyName: String?,
    val picture: String?
)