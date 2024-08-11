package com.focusmonster.controller

import org.springframework.core.io.ClassPathResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class JobController {
    @GetMapping("/jobs/list")
    fun job(): ResponseEntity<List<String>> {
        val jobList = ClassPathResource("job/job.txt")
        val jobs = String(jobList.contentAsByteArray).split("\n")
        return ResponseEntity.ok(jobs.map { it.split(",")[1].trimStart() })
    }
}