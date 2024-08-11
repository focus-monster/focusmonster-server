package com.focusmonster.service.focus.model

data class AddHistoryRequest(
    val focusId: Long,
    val history: String
)