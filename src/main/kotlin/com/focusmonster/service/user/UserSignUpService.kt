package com.focusmonster.service.user

import com.focusmonster.config.GoogleOauthProperty
import com.focusmonster.document.user.User
import com.focusmonster.document.user.UserRepository
import com.focusmonster.service.user.model.GoogleTokenRequest
import com.focusmonster.service.user.model.GoogleUserInfoResponse
import com.focusmonster.service.user.model.GoogleUserTokenResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.UUID

@Service
class UserSignUpService(
    private val userRepository: UserRepository,
    private val googleOauthProperty: GoogleOauthProperty
) {
    val restTemplate = RestTemplate()

    fun signUpWithGoogle(code: String): User {
        val userTokenResponse = getGoogleUserToken(code)
        val accessToken = userTokenResponse.accessToken
        val userInfo = getGoogleUserInfo(accessToken)

        return signUp(userInfo)
    }

    private fun getGoogleUserToken(code: String): GoogleUserTokenResponse {
        val body = GoogleTokenRequest(
            code,
            googleOauthProperty.clientId,
            googleOauthProperty.clientSecret,
            googleOauthProperty.redirectUri,
            "authorization_code"
        )

        val response = restTemplate.exchange(
            "https://oauth2.googleapis.com/token",
            HttpMethod.POST,
            HttpEntity(body),
            GoogleUserTokenResponse::class.java
        )
        return response.body!!
    }

    private fun getGoogleUserInfo(accessToken: String): GoogleUserInfoResponse {
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $accessToken")

        val response = restTemplate.exchange(
            "https://www.googleapis.com/userinfo/v2/me",
            HttpMethod.GET,
            HttpEntity<Void>(headers),
            GoogleUserInfoResponse::class.java
        )
        return response.body!!
    }

    fun signUp(request: GoogleUserInfoResponse): User {
        val socialId = request.id!!
        if (userRepository.existsBySocialId(socialId)) {
            return userRepository.findFirstBySocialIdOrderByCreatedDateTimeDesc(socialId).get()
        }

        val user = User(
            nickname = request.name,
            email = request.email,
            socialId = socialId,
            verified = false,
            successCount = 0,
            token = UUID.randomUUID().toString()
        )
        userRepository.save(user)
        return user
    }

    fun signUpAnonymousUser(): User {
        var randomUUID = UUID.randomUUID().toString()
        while(userRepository.existsBySocialId(randomUUID)) {
            randomUUID = UUID.randomUUID().toString()
        }

        val socialId = randomUUID
        if (userRepository.existsBySocialId(socialId)) {
            return userRepository.findFirstBySocialIdOrderByCreatedDateTimeDesc(socialId).get()
        }

        val user = User(
            nickname = "Anonymous",
            job = "없음",
            email = "anonymous@focusmonster.me",
            socialId = socialId,
            verified = true,
            successCount = 0,
            anonymous = true,
            token = UUID.randomUUID().toString()
        )
        userRepository.save(user)
        return user
    }
}