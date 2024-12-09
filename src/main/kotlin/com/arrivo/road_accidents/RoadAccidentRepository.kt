package com.arrivo.road_accidents

import org.springframework.data.jpa.repository.JpaRepository


interface RoadAccidentRepository : JpaRepository<RoadAccident, Long>