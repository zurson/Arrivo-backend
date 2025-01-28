package com.arrivo.task

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController(private val service: TaskService) {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    fun getAll() = ResponseEntity.ok(service.findAll())

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun create(@Valid @RequestBody taskRequest: TaskCreateRequest): ResponseEntity<TaskDTO> {
        return ResponseEntity.ok(service.create(taskRequest))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}")
    fun updateTask(
        @PathVariable id: Long,
        @Valid @RequestBody request: TaskUpdateRequest
    ): ResponseEntity<TaskDTO> {
        return ResponseEntity.ok(service.update(id, request))
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PatchMapping("/{id}")
    fun updateTaskStatus(
        @PathVariable id: Long,
        @Valid @RequestBody request: TaskStatusUpdateRequest
    ): ResponseEntity<TaskDTO> {
        return ResponseEntity.ok(service.updateTaskStatus(id, request))
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/free-tasks")
    fun getFreeTasks(): ResponseEntity<List<TaskDTO>> {
        return ResponseEntity.ok(service.getFreeTasks())
    }

}