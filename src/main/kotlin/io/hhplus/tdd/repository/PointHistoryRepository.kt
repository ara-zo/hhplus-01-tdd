package io.hhplus.tdd.repository

import io.hhplus.tdd.domain.PointHistory
import org.springframework.stereotype.Repository

@Repository
interface PointHistoryRepository {
    fun findAllById(id: Long): List<PointHistory>

}