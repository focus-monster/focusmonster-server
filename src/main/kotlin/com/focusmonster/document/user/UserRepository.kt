package com.focusmonster.document.user

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface UserRepository: MongoRepository<User, Long> {
    fun existsBySocialId(socialId: String): Boolean
    fun findFirstBySocialIdOrderByCreatedDateTimeDesc(socialId: String): Optional<User> //TODO("소셜 로그인 연동 후 fade-out")
    fun existsByNicknameAndVerified(nickname: String, verified: Boolean): Boolean
}