package com.focusmonster.service.gemini

import com.focusmonster.document.focus.FocusRepository
import com.focusmonster.document.focus.FocusStatus
import com.focusmonster.document.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class GeminiController(
    private val geminiService: GeminiService
) {
    @PostMapping("/gemini/focus")
    fun validFocus(@RequestBody validFocusUpLoadableRequest: ValidFocusUpLoadableRequest) {
        geminiService.validFocusUpLoadable(validFocusUpLoadableRequest)
    }
}

data class ValidFocusUpLoadableRequest(
    val socialId: String,
    val focusId: Long
)

@Service
class GeminiService(
    private val userRepository: UserRepository,
    private val focusRepository: FocusRepository
) {
    fun validFocusUpLoadable(request: ValidFocusUpLoadableRequest) {
        val user = userRepository.findFirstBySocialIdOrderByCreatedDateTimeDesc(request.socialId)
            .orElseThrow { IllegalArgumentException("User Not Found") }

        val focus = focusRepository.findById(request.focusId)
            .orElseThrow { IllegalArgumentException("Focus Not Found") }

        if (focus.focusStatus != FocusStatus.FOCUSING) {
            throw IllegalStateException("Not Focusing")
        }

        if (user.socialId != focus.userSocialId) {
            throw IllegalStateException("Not User's Focus")
        }

        val seconds = LocalDateTime.now().minusSeconds(10)
        if (focus.lastModifiedDateTime!!.isAfter(seconds)) {
            throw IllegalStateException("Too Many Request")
        }
    }
}
