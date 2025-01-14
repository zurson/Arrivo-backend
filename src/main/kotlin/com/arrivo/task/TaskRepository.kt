package com.arrivo.task

import org.springframework.data.jpa.repository.JpaRepository

interface TaskRepository : JpaRepository<Task, Long> {

    fun findAllByStatus(status: TaskStatus): List<Task>

}