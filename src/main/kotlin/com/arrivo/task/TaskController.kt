package com.arrivo.task

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController(private val service: TaskService) {
    @GetMapping
    fun getAll() = ResponseEntity.ok(service.findAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) = service.findById(id)

    @PostMapping
    fun create(@Valid @RequestBody taskRequest: TaskCreateRequest): ResponseEntity<Task> {
        return ResponseEntity.ok(service.create(taskRequest))
    }

    @PatchMapping("/{id}")
    fun updateTask(
        @PathVariable id: Long,
        @Valid @RequestBody request: TaskUpdateRequest
    ): ResponseEntity<Task> {
        return ResponseEntity.ok(service.update(id, request))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = ResponseEntity.ok(service.deleteById(id))
}