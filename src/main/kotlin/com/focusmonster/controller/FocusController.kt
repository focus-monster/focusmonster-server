package com.focusmonster.controller

import com.focusmonster.document.focus.Focus
import com.focusmonster.document.focus.FocusTime
import com.focusmonster.service.focus.FocusEvaluationService
import com.focusmonster.service.focus.FocusService
import com.focusmonster.service.focus.model.AddHistoryRequest
import com.focusmonster.service.focus.model.EndFocusRequest
import com.focusmonster.service.focus.model.StartFocusRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class FocusController(
    val focusService: FocusService,
    val focusEvaluationService: FocusEvaluationService
) {
    @PostMapping("/focus")
    fun createFocus(@RequestBody request: StartFocusRequest): ResponseEntity<Focus> {
        return ResponseEntity.ok(focusService.startFocus(request))
    }

    @PostMapping("/focus/big-succeed")
    fun bigSuccessFocus(@RequestBody request: EndFocusRequest): ResponseEntity<Focus> {
        val focus = focusService.bigSucceedFocus(request)
        return ResponseEntity.ok(focusEvaluationService.evaluateHistory(focusId = focus.id!!))
    }

    @PostMapping("/focus/succeed")
    fun successFocus(@RequestBody request: EndFocusRequest): ResponseEntity<Focus> {
        val focus = focusService.succeedFocus(request)
        return ResponseEntity.ok(focusEvaluationService.evaluateHistory(focusId = focus.id!!))
    }

    @PostMapping("/focus/fail")
    fun failFocus(@RequestBody request: EndFocusRequest): ResponseEntity<Focus> {
        val focus = focusService.failFocus(request)
        return ResponseEntity.ok(focusEvaluationService.evaluateHistory(focusId = focus.id!!))
    }

    @PostMapping("/focus/history")
    fun addHistory(@RequestBody request: AddHistoryRequest) {
        return focusService.addHistory(request.focusId, request.history)
    }

    @GetMapping("/focus")
    fun findAllFocus(@RequestParam("socialId") socialId: String): ResponseEntity<List<Focus>> {
        return ResponseEntity.ok(focusService.findAllBySocialId(socialId))
    }

    @GetMapping("/focus/brief")
    fun briefFocus(@RequestParam("focusId") focusId: Long): ResponseEntity<String> {
        return ResponseEntity.ok(focusEvaluationService.getFocusBrief(focusId))
    }

    @GetMapping("/focus/today-time")
    fun findTodayFocusTime(@RequestParam("socialId") socialId: String): ResponseEntity<FocusTime> {
        return ResponseEntity.ok(focusService.findTodayFocusTime(socialId))
    }
}