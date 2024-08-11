package com.focusmonster.service.image

import com.focusmonster.document.image.Image
import com.focusmonster.document.image.ImageStatus
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Service

@Service
class NcpImageService(
    private val mongoTemplate: MongoTemplate
) {
    fun getImage(imageStatus: ImageStatus): String? {
        val matchStage = Aggregation.match(Criteria.where("status").`is`(imageStatus))
        val sampleStage = Aggregation.sample(1)

        val aggregation = Aggregation.newAggregation(matchStage, sampleStage)

        val result = mongoTemplate.aggregate(aggregation, "image", Image::class.java)
        return result.mappedResults.first().url
    }
}



