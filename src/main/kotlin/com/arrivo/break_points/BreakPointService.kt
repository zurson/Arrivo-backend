package com.arrivo.break_points

import com.arrivo.utilities.IdNotFoundException
import org.springframework.stereotype.Service

@Service
class BreakPointService(private val repository: BreakPointRepository) {

    fun findAll(): List<BreakPoint> = repository.findAll()

    fun save(request: BreakPointRequest) {
        val existingBreakPoints = repository.findAll().associateBy { it.location }

        request.locations.forEach { location ->
            if (!existingBreakPoints.containsKey(location)) {
                try {
                    repository.save(BreakPoint(location = location))
                } catch (ex: Exception) {
                    println("Error saving location lat: ${location.latitude}, lon: ${location.longitude}: ${ex.message}")
                }
            } else {
                println("Skipping Location lat: ${location.latitude}, lon: ${location.longitude} - already exists")
            }
        }
    }


    fun findById(id: Long): BreakPoint {
        return repository.findById(id).orElseThrow {
            IdNotFoundException("Break Point with ID $id not found")
        }
    }

    fun delete(id: Long) {
        findById(id).let { repository.delete(it) }
    }
}
