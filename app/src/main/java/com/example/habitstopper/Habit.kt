package com.example.habitstopper

data class Habit(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val iconName: String = "default",
    val colorHex: String = "#FFE0E0",
    val streak: Int = 0,
    val checkedToday: Boolean = false,
    val lastCheckedDate: String = "",
    val createdAt: Long = 0L

)