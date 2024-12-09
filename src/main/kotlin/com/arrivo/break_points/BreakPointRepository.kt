package com.arrivo.break_points

import com.arrivo.utilities.Location
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BreakPointRepository : JpaRepository<BreakPoint, Long> {

    fun findByLocation(location: Location): Optional<BreakPoint>

}