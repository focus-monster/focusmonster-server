package com.focusmonster.document.focus

import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDateTime
import java.util.Optional

interface FocusRepository: MongoRepository<Focus, Long> {
    fun findAllByUserSocialId(socialId: String): List<Focus>

    fun countAllByUserSocialIdAndCreatedDateTimeAfter(socialId: String, dateTime: LocalDateTime): Int

    fun findAllByUserSocialIdAndCreatedDateTimeAfter(socialId: String, dateTime: LocalDateTime): List<Focus>

    fun findFirstByUserSocialIdOrderByCreatedDateTimeDesc(socialId: String): Optional<Focus>
}