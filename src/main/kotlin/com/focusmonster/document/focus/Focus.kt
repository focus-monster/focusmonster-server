package com.focusmonster.document.focus

import com.focusmonster.document.BaseEntity
import com.focusmonster.document.SequenceGeneratorService
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent
import org.springframework.stereotype.Component


@Document(collection = "focus")
open class Focus(
    @Id
    var id: Long? = null,
    var userSocialId: String? = null,
    var duration: FocusTime? = null,
    var banedSiteAccessLog: MutableList<SiteAccessLog> = mutableListOf(),
    var history: MutableList<String> = mutableListOf(),

    var focusStatus: FocusStatus = FocusStatus.FOCUSING,
    var image: String? = null,
    var evaluation: String? = null,
    var resultDuration: FocusTime? = null
) : BaseEntity() {
    companion object {
        @Transient
        val SEQUENCE_NAME = "focus_sequence"
    }
}

@Component
class FocusListener(
    private val generatorService: SequenceGeneratorService
) : AbstractMongoEventListener<Focus>() {

    override fun onBeforeConvert(event: BeforeConvertEvent<Focus>) {
        val focus = event.source
        if (focus.id == null) {
            focus.id = generatorService.generateSequence(Focus.SEQUENCE_NAME)
        }
    }
}

enum class FocusStatus {
    FAILED, SUCCEED, FOCUSING
}

data class FocusTime(
    val hours: Int,
    val minutes: Int
)