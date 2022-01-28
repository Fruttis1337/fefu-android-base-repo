package ru.fefu.activitytracker.data

data class UserModel (
    val id: Long,
    val name: String,
    val login: String,
    val gender: GenderModel,
)
