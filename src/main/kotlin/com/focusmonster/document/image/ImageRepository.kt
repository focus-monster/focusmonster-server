package com.focusmonster.document.image

import org.springframework.data.mongodb.repository.MongoRepository

interface ImageRepository: MongoRepository<Image, Long> {
}