package com.focusmonster.service.focus.model

import com.focusmonster.document.focus.SiteAccessLog

data class EndFocusRequest(
    val focusId: Long,
    val socialId: String,
    val banedSiteAccessLog: List<SiteAccessLog>
)