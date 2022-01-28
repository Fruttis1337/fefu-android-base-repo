package ru.fefu.activitytracker.data

import java.time.LocalDateTime

data class UserActivityData(
    val distance: String,
    val activityType: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val user: String,
)
