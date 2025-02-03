package com.arrivo.work_time_analysis

import com.arrivo.company.Company
import com.arrivo.delivery.Delivery
import com.arrivo.delivery.DeliveryRepository
import com.arrivo.delivery.DeliveryStatus
import com.arrivo.employee.Employee
import com.arrivo.firebase.FirebaseService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.temporal.ChronoUnit


@Service
class WorkTimeAnalysisService(
    @Lazy private val deliveryRepository: DeliveryRepository,
    @Lazy private val firebaseService: FirebaseService
) {

    @Transactional
    fun getEmployeesWorkingHours(startDate: LocalDate, endDate: LocalDate): List<WorkingHoursDTO> {
        validateDateRange(startDate, endDate)

        val company = firebaseService.getUserCompany()
        val completedDeliveries = fetchCompletedDeliveries(startDate, endDate, company)

        val employeeWorkingHours = calculateWorkingHours(completedDeliveries)

        return employeeWorkingHours.toSortedList()
    }


    private fun validateDateRange(startDate: LocalDate, endDate: LocalDate) {
        require(!startDate.isAfter(endDate)) {
            "Start date ($startDate) cannot be after end date ($endDate)"
        }
    }


    private fun fetchCompletedDeliveries(startDate: LocalDate, endDate: LocalDate, company: Company) =
        deliveryRepository.findByAssignedDateBetweenAndStatusAndCompany(
            startDate = startDate,
            endDate = endDate,
            status = DeliveryStatus.COMPLETED,
            company = company
        )


    private fun calculateWorkingHours(deliveries: List<Delivery>): Map<Employee, Float> {
        return deliveries
            .mapNotNull { delivery ->
                delivery.startDate?.let { start ->
                    delivery.endDate?.let { end ->
                        delivery.employee to ChronoUnit.MINUTES.between(start, end)
                    }
                }
            }
            .groupBy({ it.first }, { it.second })
            .mapValues { (_, minutesList) ->
                val totalMinutes = minutesList.sum()
                roundToTwoDecimals(totalMinutes / 60.0)
            }
    }


    private fun Map<Employee, Float>.toSortedList(): List<WorkingHoursDTO> =
        this.entries
            .sortedByDescending { it.value }
            .map { (employee, hours) ->
                WorkingHoursDTO(
                    firstName = employee.firstName,
                    lastName = employee.lastName,
                    hours = hours
                )
            }


    private fun roundToTwoDecimals(value: Double): Float =
        BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toFloat()
}
