package com.focusmonster.service.user

import com.focusmonster.document.focus.FocusRepository
import com.focusmonster.document.focus.FocusStatus
import com.focusmonster.document.user.User
import com.focusmonster.document.user.UserRepository
import com.focusmonster.service.user.model.SuccessOnboardingRequest
import com.focusmonster.service.user.model.UpdateUserRequest
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val focusRepository: FocusRepository,
    private val userValidateService: UserValidateService,
) {
    fun successOnboarding(request: SuccessOnboardingRequest): User {
        userValidateService.validSuccessOnboardingRequest(request)

        val user = userRepository.findFirstBySocialIdOrderByCreatedDateTimeDesc(request.socialId)
            .orElseThrow{ IllegalArgumentException("user not found") }
        user.nickname = request.nickname
        user.job = request.job
        user.verified = true
        userRepository.save(user)

        return user.resolveLevel()
    }

    fun updateUser(request: UpdateUserRequest): User {
        val user = userRepository.findFirstBySocialIdOrderByCreatedDateTimeDesc(request.socialId)
            .orElseThrow{ IllegalArgumentException("user not found") }

        request.nickname?.let {
            if (request.nickname != user.nickname!!) {
                userValidateService.validNickname(request.nickname)
                user.nickname = request.nickname
            }
        }

        request.job?.let {
            if (request.job != user.job!!) {
                userValidateService.validJob(request.job)
                user.job = request.job
            }
        }

        userRepository.save(user)
        return user.resolveLevel()
    }

    fun findAllUsers(): List<User> {
        return userRepository.findAll().map { user -> user.resolveLevel() }
    }

    fun findUserBySocialId(socialId: String): User {
        val user = userRepository.findFirstBySocialIdOrderByCreatedDateTimeDesc(socialId)
            .orElseThrow { IllegalArgumentException("Invalid SocialId") }
        return user.resolveLevel()
    }

    private fun getUserLevel(socialId: String): Int {
        return focusRepository.findAllByUserSocialId(socialId)
            .map { it.focusStatus }.count { FocusStatus.SUCCEED == it }
    }

    fun User.resolveLevel(): User {
        this.level = getUserLevel(this.socialId!!)
        return this
    }
}