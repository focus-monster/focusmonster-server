package com.focusmonster

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing

@SpringBootApplication
@EnableMongoAuditing
@ConfigurationPropertiesScan
class FocusMonsterApplication

fun main(args: Array<String>) {
    runApplication<FocusMonsterApplication>(*args)
}


