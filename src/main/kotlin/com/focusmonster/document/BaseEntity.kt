package com.focusmonster.document

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import java.time.LocalDateTime

open class BaseEntity {
    @Version
    @JsonIgnore
    var version: Int? = null

    @CreatedDate
    var createdDateTime: LocalDateTime? = null

    @LastModifiedDate
    var lastModifiedDateTime: LocalDateTime? = null
}
