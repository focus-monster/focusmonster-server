package com.focusmonster.service.focus

import com.focusmonster.document.focus.Focus
import com.focusmonster.document.focus.FocusRepository
import com.focusmonster.document.focus.FocusStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class FocusEvaluationService(
    private val focusRepository: FocusRepository,
    @Value("\${gemini-server.host}") val geminiServerHost: String
) {
    fun evaluateHistory(focusId: Long): Focus {
        val focus = findFocus(focusId)

        if (focus.focusStatus == FocusStatus.FOCUSING) {
            throw IllegalStateException("must end focusing")
        }

        val history = getFocusBrief(focusId)
        val evaluation = doEvaluate(history)

        focus.evaluation = evaluation.body!!
        focusRepository.save(focus)
        return focus
    }

    private fun doEvaluate(history: String): ResponseEntity<String> {
        val restTemplate = RestTemplate()

        data class EvaluationRequest(val history: String)

        return restTemplate.exchange(
            "http://$geminiServerHost/evaluate",
            HttpMethod.POST,
            HttpEntity(EvaluationRequest(history)),
            String::class.java
        )
    }

    fun getFocusBrief(focusId: Long): String {
        val focus = findFocus(focusId)

        return """
            $systemContext_en
            ${focus.history.joinToString(" ")}
        """.trim()
    }

    private fun findFocus(focusId: Long): Focus = focusRepository.findById(focusId)
        .orElseThrow { IllegalArgumentException("Focus Not Found") }
}