package com.focusmonster.service.focus.model

import com.focusmonster.document.focus.FocusTime

data class StartFocusRequest(
    val socialId: String,
    val duration: FocusTime,
    val banedSites: MutableList<String> = mutableListOf(),
    val task: String?
)