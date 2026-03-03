package com.example.habitstopper

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.os.Build
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.O)
class HabitViewModel (
    private val repository: HabitRepository = HabitRepository()
): ViewModel(){
    //the list starts empty n fills when loadhabits is called
    var habits by mutableStateOf<List<Habit>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    //called when the screens opens
    fun loadHabits(){
        viewModelScope.launch {
            try {
                isLoading = true

                errorMessage = null
                habits = repository.getHabits()
            } catch (e: Exception){
                errorMessage = e.message ?: "Failed to load habits"
            }finally {
                isLoading = false
            }
        }
    }

    //called when user taps + new habit to break
    fun addHabit(name: String, iconName: String, colorHex: String){
        viewModelScope.launch {
            try {
                repository.addHabit(name, iconName, colorHex)
                loadHabits()
            }catch (e: Exception){
                errorMessage = e.message ?: "Failed to add Habit"
            }
        }
    }

    //called when user deletes a habit
    fun deleteHabit(habitId: String){
        viewModelScope.launch {
            try {
                repository.deleteHabit(habitId)
                loadHabits()
            }catch (e: Exception){
                errorMessage = e.message ?: "Failed to delete habit"
            }
        }
    }

    //called when user checks or unchecks a habit
    fun checkHabit(habit: Habit){
        viewModelScope.launch {
            try {
                repository.checkHabit(habit.id, habit)
                loadHabits()
            }catch (e: Exception){
                errorMessage = e.message ?: "Failed to update habit"
            }
        }
    }

}