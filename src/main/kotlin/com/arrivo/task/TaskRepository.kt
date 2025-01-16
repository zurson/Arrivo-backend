package com.arrivo.task

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TaskRepository : JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.delivery IS NULL")
    fun findAllByDeliveryIsNull(): List<Task>

}