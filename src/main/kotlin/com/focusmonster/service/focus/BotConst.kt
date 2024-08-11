package com.focusmonster.service.focus

import com.focusmonster.document.focus.FocusTime

const val systemContext_kr = "Internal Script"

const val systemContext_en = "Internal Script"

const val bigSuccessContext_kr = "Internal Script"
const val bigSuccessContext_en = "Internal Script"
const val successContext_kr = "Internal Script"
const val successContext_en = "Internal Script"
const val failureContext_kr = "Internal Script"
const val failureContext_en = "Internal Script"

fun startFocusContext_en(
    job: String,
    duration: FocusTime,
    task: String? = null
): MutableList<String> {
    val focusContext = mutableListOf("Internal Script")
    return focusContext
}

fun startFocusContext_kr(
    job: String,
    duration: FocusTime,
    task: String? = null
): MutableList<String> {
    val focusContext = mutableListOf("Internal Script".trimIndent())

    return focusContext
}
