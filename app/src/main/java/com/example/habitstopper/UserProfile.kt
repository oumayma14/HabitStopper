package com.example.habitstopper

data class UserProfile(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val joinDate: Long = 0L,
    val currentStreak: Int = 0
)
