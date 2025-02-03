package com.arrivo.task

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TaskRepository : JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.delivery IS NULL AND t.company.id = :companyId")
    fun findAllByDeliveryIsNull(companyId: Long): List<Task>

    @Query("SELECT t FROM Task t WHERE t.company.id = :companyId")
    fun findAllTasksInCompany(companyId: Long): List<Task>
}