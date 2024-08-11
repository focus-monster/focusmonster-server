package com.focusmonster.service.user

import com.focusmonster.document.user.UserRepository
import com.focusmonster.service.user.model.SuccessOnboardingRequest
import org.springframework.stereotype.Service

@Service
class UserValidateService(
    private val userRepository: UserRepository
) {
    fun checkJob(job: String) = job.isOverBytes(50)

    fun checkTask(task: String) = task.isOverBytes(500)

    fun checkNickname(nickname: String): List<String> {
        val result: MutableList<String> = mutableListOf()
        val regex = Regex("^[a-zA-Z0-9가-힣]*$")

        if (!regex.matches(nickname)) {
            result.add("C1(영어, 한국어, 숫자 조합만 가능)")
        }
        if (nickname.isOverBytes(13)) {
            result.add("C2(글자 수 초과)")
        }
        if (userRepository.existsByNicknameAndVerified(nickname, true)) {
            result.add("C3(중복 닉네임)")
        }

        return result
    }

    fun validSuccessOnboardingRequest(request: SuccessOnboardingRequest)  {
        validNickname(request.nickname)
        validJob(request.job)
    }

    fun validNickname(nickname: String) {
        if (checkNickname(nickname).isNotEmpty()) {
            throw IllegalArgumentException("Invalid Nickname")
        }
    }

    fun validJob(job: String) {
        if (checkJob(job)) {
            throw IllegalArgumentException("Invalid Job")
        }
    }

    private fun validTask(task: String) {
        if (checkTask(task)) {
            throw IllegalArgumentException("Invalid Task")
        }
    }

    private fun String.isOverBytes(byte: Int) = this.toByteArray(Charsets.UTF_8).size > byte
}

/**
 * 30초마다 이미지 -> 600장
 */