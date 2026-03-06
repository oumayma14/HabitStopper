package com.example.habitstopper

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class HabitRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val userId get() = auth.currentUser?.uid ?: ""
    private val habitsCollection
        get() = db.collection("users").document(userId).collection("habits")

    // fetch all habits for the current user
    suspend fun getHabits(): List<Habit> {
        val snapshot = habitsCollection.get().await()
        val today = LocalDate.now().toString()
        val yesterday = LocalDate.now().minusDays(1).toString()

        val habits = snapshot.documents.mapNotNull { doc ->
            doc.toObject(Habit::class.java)?.copy(id = doc.id)
        }

        habits.forEach { habit ->
            val lastChecked = habit.lastCheckedDate

            // reset checkedToday if it wasn't checked today
            if (habit.checkedToday && lastChecked != today) {
                habitsCollection.document(habit.id).update("checkedToday", false).await()
            }

            // reset streak if last check was more than 1 day ago
            // meaning they missed yesterday AND today
            if (habit.streak > 0 && lastChecked != today && lastChecked != yesterday) {
                habitsCollection.document(habit.id).update("streak", 0).await()
            }
        }

        // return habits with local corrections applied
        return habits.map { habit ->
            val lastChecked = habit.lastCheckedDate
            habit.copy(
                checkedToday = if (habit.checkedToday && lastChecked != today) false else habit.checkedToday,
                streak = if (habit.streak > 0 && lastChecked != today && lastChecked != yesterday) 0 else habit.streak
            )
        }
    }

    // add a new habit to Firestore
    suspend fun addHabit(name: String, iconName: String, colorHex: String) {
        val newHabit = Habit(
            userId = userId,
            name = name,
            iconName = iconName,
            colorHex = colorHex,
            createdAt = System.currentTimeMillis()
        )
        habitsCollection.add(newHabit).await()
    }

    // delete a habit by its ID — just removes the document
    suspend fun deleteHabit(habitId: String) {
        habitsCollection.document(habitId).delete().await()
    }

    // check or uncheck a habit and update streak
    suspend fun checkHabit(habitId: String, currentHabit: Habit) {
        val today = LocalDate.now().toString()
        val wasCheckedYesterday = currentHabit.lastCheckedDate ==
                LocalDate.now().minusDays(1).toString()

        // if already checked today → unchecking, so decrease streak
        // if not checked → checking, so increase or start streak
        val newStreak = when {
            currentHabit.checkedToday -> (currentHabit.streak - 1).coerceAtLeast(0)
            wasCheckedYesterday -> currentHabit.streak + 1
            else -> 1
        }

        habitsCollection.document(habitId).update(
            mapOf(
                "checkedToday" to !currentHabit.checkedToday,
                "lastCheckedDate" to today,
                "streak" to newStreak
            )
        ).await()
    }
}