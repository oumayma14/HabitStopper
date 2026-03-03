package com.example.habitstopper.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.habitstopper.Habit
import com.example.habitstopper.HabitViewModel
import com.example.habitstopper.UserViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController) {
    var showAddHabitSheet by remember { mutableStateOf(false) }
    val userViewModel: UserViewModel = viewModel()
    val habitViewModel: HabitViewModel = viewModel()

    LaunchedEffect(Unit) {
        userViewModel.loadUserProfile()
        habitViewModel.loadHabits()
    }

    val userProfile = userViewModel.userProfile
    val habits = habitViewModel.habits
    val isLoading = habitViewModel.isLoading
    val auth = com.google.firebase.auth.FirebaseAuth.getInstance()

    val today = LocalDate.now()
    val dayName = today.format(DateTimeFormatter.ofPattern("EEEE"))
    val dayNumber = today.dayOfMonth
    val year = today.year

    fun getDaySuffix(day: Int): String {
        return if (day in 11..13) "th"
        else when (day % 10) {
            1 -> "st"; 2 -> "nd"; 3 -> "rd"; else -> "th"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F4F0))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // HEADER
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E))
                            )
                        )
                        .padding(horizontal = 24.dp, vertical = 36.dp)
                ) {
                    Column {
                        Text(
                            text = if (userProfile != null) "Hey, ${userProfile.displayName} 👋" else "Hey there 👋",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "$dayName, ${dayNumber}${getDaySuffix(dayNumber)} $year",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(Modifier.height(20.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            StatChip("Habits", habits.size.toString(), Color(0xFF6C63FF))
                            StatChip("Done today", habits.count { it.checkedToday }.toString(), Color(0xFF00D4AA))
                            StatChip("Best streak", "🔥 ${habits.maxOfOrNull { it.streak } ?: 0}", Color(0xFFFF6B6B))
                        }
                    }
                }
            }

            // SECTION TITLE + ADD BUTTON
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today's Habits",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A2E)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF6C63FF), Color(0xFF00D4AA))
                                )
                            )
                            .clickable { showAddHabitSheet = true }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("New Habit", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }

            // LOADING
            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF6C63FF))
                    }
                }
            }

            // EMPTY STATE
            if (!isLoading && habits.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎯", fontSize = 48.sp)
                        Spacer(Modifier.height(12.dp))
                        Text("No habits yet", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1A1A2E))
                        Text("Add your first habit to break!", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            }

            // HABIT CARDS
            items(habits) { habit ->
                HabitCard(
                    habit = habit,
                    onCheckedChange = { habitViewModel.checkHabit(habit) },
                    onDelete = { habitViewModel.deleteHabit(habit.id) }
                )
            }

            // SIGN OUT
            item {
                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = {
                        auth.signOut()
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                ) {
                    Text("Sign out", color = Color.Gray, fontSize = 14.sp)
                }
            }
        }

        if (showAddHabitSheet) {
            AddHabitSheet(
                onDismiss = { showAddHabitSheet = false },
                onAdd = { name, iconName, colorHex ->
                    habitViewModel.addHabit(name, iconName, colorHex)
                    showAddHabitSheet = false
                }
            )
        }
    }
}

@Composable
fun StatChip(label: String, value: String, color: Color) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, color = color, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
        Text(label, color = color.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun HabitCard(
    habit: Habit,
    onCheckedChange: () -> Unit,
    onDelete: () -> Unit
) {
    val cardGradient = remember(habit.colorHex) {
        try {
            val color = Color(android.graphics.Color.parseColor(habit.colorHex))
            Brush.linearGradient(colors = listOf(color, Color(0xFFA855F7)))
        } catch (e: Exception) {
            Brush.linearGradient(colors = listOf(Color(0xFFFF4757), Color(0xFFA855F7)))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(cardGradient)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${habit.iconName} ${habit.name}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (habit.streak > 0) "🔥 ${habit.streak} day streak" else "Start your streak today!",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.75f)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Checkbox(
                    checked = habit.checkedToday,
                    onCheckedChange = { onCheckedChange() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.White,
                        uncheckedColor = Color.White.copy(alpha = 0.6f),
                        checkmarkColor = Color(0xFF1A1A2E)
                    )
                )
            }
        }
    }
}