package com.focusmonster.service.focus

import com.focusmonster.document.focus.*
import com.focusmonster.document.image.ImageStatus
import com.focusmonster.service.focus.model.EndFocusRequest
import com.focusmonster.service.focus.model.StartFocusRequest
import com.focusmonster.service.image.NcpImageService
import com.focusmonster.service.user.UserService
import org.springframework.stereotype.Service
import java.lang.StringBuilder
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class FocusService(
    private val userService: UserService,
    private val focusRepository: FocusRepository,
    private val imageService: NcpImageService,
) {
    fun startFocus(request: StartFocusRequest): Focus {
        validateRequest(request)

        val user = userService.findUserBySocialId(request.socialId)
        val job = user.job!!

        val focus = Focus(
            userSocialId = request.socialId,
            duration = request.duration,
            banedSiteAccessLog = request.banedSites.map { SiteAccessLog(it) }.toMutableList(),
            history = startFocusContext_en(job, request.duration, request.task)
        )
        focusRepository.save(focus)
        return focus
    }

    private fun validateRequest(request: StartFocusRequest) {
        val todayFocusCount = focusRepository.countAllByUserSocialIdAndCreatedDateTimeAfter(
                request.socialId,
                LocalDate.now().atStartOfDay()
            )

        if (todayFocusCount > 20) {
            throw IllegalStateException("you can create 20 focus per day")
        }

        val lastFocus =
            focusRepository.findFirstByUserSocialIdOrderByCreatedDateTimeDesc(socialId = request.socialId)

        if (lastFocus.isPresent) {
            if (!lastFocus.get().createdDateTime!!.isBefore(LocalDateTime.now().minusMinutes(1))) {
                throw IllegalStateException("you can create focus in 1 minutes")
            }
        }
    }

    /**
     * 화면 기록을 참고해서 현재 집중 상태 파악
     */
    fun addHistory(focusId: Long, message: String) {
        val focus = findFocus(focusId = focusId)
        focus.history.add(message)
        focusRepository.save(focus)
    }

    fun bigSucceedFocus(request: EndFocusRequest): Focus {
        val focus = findFocus(request.focusId)

        if (focus.focusStatus != FocusStatus.FOCUSING) {
            throw IllegalStateException("Focusing Already Done")
        }

        updateBanedSiteAccessLog(focus, request.banedSiteAccessLog)
        bigSucceedFocus(focus)
        focusRepository.save(focus)
        return focus
    }

    private fun bigSucceedFocus(focus: Focus) {
        focus.history.add(bigSuccessContext_en)
        focus.image = getBigSuccessImage()
        focus.focusStatus = FocusStatus.SUCCEED

        val focusDuration = getFocusDuration(focus)
        focus.resultDuration = FocusTime(focusDuration.toHoursPart(), focusDuration.toMinutesPart())
    }

    private fun getBigSuccessImage() = imageService.getImage(ImageStatus.SUCCESS)

    fun succeedFocus(request: EndFocusRequest): Focus {
        val focus = findFocus(request.focusId)

        if (focus.focusStatus != FocusStatus.FOCUSING) {
            throw IllegalStateException("Focusing Already Done")
        }

        updateBanedSiteAccessLog(focus, request.banedSiteAccessLog)
        succeedFocus(focus)
        focusRepository.save(focus)
        return focus
    }


    private fun succeedFocus(focus: Focus) {
        focus.history.add(successContext_en)
        focus.image = getSuccessImage()
        focus.focusStatus = FocusStatus.SUCCEED

        val focusDuration = getFocusDuration(focus)
        focus.resultDuration = FocusTime(focusDuration.toHoursPart(), focusDuration.toMinutesPart())
    }

    private fun getSuccessImage() = imageService.getImage(ImageStatus.SUCCESS)

    fun failFocus(request: EndFocusRequest): Focus {
        val focus = findFocus(request.focusId)

        if (focus.focusStatus != FocusStatus.FOCUSING) {
            throw IllegalStateException("Focusing Already Done")
        }

        updateBanedSiteAccessLog(focus, request.banedSiteAccessLog)
        failFocus(focus)
        focusRepository.save(focus)
        return focus
    }

    private fun failFocus(focus: Focus) {
        focus.history.add(failureContext_en)
        focus.image = getFailedImage()
        focus.focusStatus = FocusStatus.FAILED

        val focusDuration = getFocusDuration(focus)
        focus.resultDuration = FocusTime(focusDuration.toHoursPart(), focusDuration.toMinutesPart())
    }

    private fun getFocusDuration(focus: Focus): Duration = Duration.between(focus.createdDateTime, LocalDateTime.now())

    private fun getFailedImage() = imageService.getImage(ImageStatus.FAILURE)

    private fun updateBanedSiteAccessLog(focus: Focus, banedSiteAccessLog: List<SiteAccessLog>) {
        focus.banedSiteAccessLog = banedSiteAccessLog.toMutableList()
        val sb = StringBuilder()
        for (siteAccessLog in focus.banedSiteAccessLog) {
            sb.append("The user attempted to  access a restricted ${siteAccessLog.name} site ${siteAccessLog.count} times\n\n")
        }
        focus.history.add(sb.toString())
    }

    private fun findFocus(focusId: Long): Focus = focusRepository.findById(focusId)
        .orElseThrow { IllegalArgumentException("Focus Not Found") }

    fun findAllBySocialId(socialId: String): List<Focus> {
        return focusRepository.findAllByUserSocialId(socialId)
    }

    fun findTodayFocusTime(socialId: String): FocusTime {
        var hours = 0
        var minutes = 0

        focusRepository.findAllByUserSocialIdAndCreatedDateTimeAfter(socialId, LocalDate.now().atStartOfDay())
            .filter { it.focusStatus == FocusStatus.SUCCEED }
            .filter { it.resultDuration != null }
            .map { it.resultDuration!! }
            .forEach {
                hours += it.hours
                minutes += it.minutes
            }

        hours += minutes / 60
        minutes %= 60

        return FocusTime(hours, minutes)
    }
}