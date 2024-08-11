package com.focusmonster.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("google")
class GoogleOauthProperty(
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String
)