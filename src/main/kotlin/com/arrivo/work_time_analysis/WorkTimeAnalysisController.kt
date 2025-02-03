package com.arrivo.work_time_analysis

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/analysis")
class WorkTimeAnalysisController(
    private val workTimeAnalysisService: WorkTimeAnalysisService
) {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/working-hours")
    fun getEmployeesWorkingHoursBetweenDates(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate
    ): ResponseEntity<List<WorkingHoursDTO>> {
        val result = workTimeAnalysisService.getEmployeesWorkingHours(startDate, endDate)
        return ResponseEntity.ok(result)
    }
}