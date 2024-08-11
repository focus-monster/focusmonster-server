package com.focusmonster.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "auto_sequence")
open class AutoIncrementSequence {
    @Id
    var id: String? = null
    var seq: Long? = null
}