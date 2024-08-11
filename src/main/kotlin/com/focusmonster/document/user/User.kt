package com.focusmonster.document.user

import com.focusmonster.document.BaseEntity
import com.focusmonster.document.SequenceGeneratorService
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent
import org.springframework.stereotype.Component


@Document(collection = "users")
open class User(
    @Id
    var id: Long? = null,
    var nickname: String? = null,
    var email: String? = null,
    var socialId: String? = null,
    var job: String? = null,
    var successCount: Int? = null,

    var level: Int? = null,

    var anonymous: Boolean = false,
    var verified: Boolean? = null,
    var token: String? = null
) : BaseEntity() {
    companion object {
        @Transient
        val SEQUENCE_NAME = "users_sequence"
    }
}

@Component
class UserListener(
    private val generatorService: SequenceGeneratorService
) : AbstractMongoEventListener<User>() {

    override fun onBeforeConvert(event: BeforeConvertEvent<User>) {
        val user = event.source
        if (user.id == null) {
            user.id = generatorService.generateSequence(User.SEQUENCE_NAME)
        }
    }
}