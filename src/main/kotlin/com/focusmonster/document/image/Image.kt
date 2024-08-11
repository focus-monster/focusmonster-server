package com.focusmonster.document.image

import com.focusmonster.document.BaseEntity
import com.focusmonster.document.SequenceGeneratorService
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent
import org.springframework.stereotype.Component


@Document(collection = "image")
open class Image(
    @Id
    var id: Long? = null,
    var url: String? = null,
    var status: ImageStatus? = null
) : BaseEntity() {
    companion object {
        @Transient
        val SEQUENCE_NAME = "image_sequence"
    }
}

@Component
class ImageListener(
    private val generatorService: SequenceGeneratorService
) : AbstractMongoEventListener<Image>() {

    override fun onBeforeConvert(event: BeforeConvertEvent<Image>) {
        val focus = event.source
        if (focus.id == null) {
            focus.id = generatorService.generateSequence(Image.SEQUENCE_NAME)
        }
    }
}

enum class ImageStatus {
    FAILURE, SUCCESS, BIG_SUCCESS
}