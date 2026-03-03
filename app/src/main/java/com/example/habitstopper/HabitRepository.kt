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
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Habit::class.java)?.copy(id = doc.id)
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