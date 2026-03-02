package com.example.habitstopper.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitstopper.HabitCard
import com.example.habitstopper.HabitItemCard
import com.example.habitstopper.habits
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController) {
    val checkedStates = remember { mutableStateMapOf<String, Boolean>() }
    var streak by remember { mutableStateOf(0) }

    val today = LocalDate.now()
    val dayName = today.format(DateTimeFormatter.ofPattern("EEEE"))
    val dayNumber = today.dayOfMonth
    val year = today.year

    val auth = com.google.firebase.auth.FirebaseAuth.getInstance()

    fun getDaySuffix(day: Int): String {
        return if (day in 11..13) "th"
        else when (day % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }

    val formattedSecondLine = "$dayNumber${getDaySuffix(dayNumber)}, $year"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 40.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(dayName, style = MaterialTheme.typography.headlineSmall)
                Text(formattedSecondLine, style = MaterialTheme.typography.headlineSmall)
            }

            val streakColor =
                if (streak > 0) Color(0xFFFF9800) else MaterialTheme.colorScheme.onBackground

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { streak++ }
            ) {
                Icon(
                    imageVector = Icons.Filled.Whatshot,
                    contentDescription = "Streak",
                    modifier = Modifier.size(50.dp),
                    tint = streakColor
                )
                Text(
                    text = streak.toString(),
                    modifier = Modifier.padding(start = 6.dp),
                    color = streakColor
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                auth.signOut()
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE57373),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(45.dp)
        ) {
            Text("Sign out")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("What did you resist today ?", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "+ New Habit to Break",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEAEAEA))
                    .clickable {}
                    .padding(vertical = 10.dp, horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            habits.forEach { habit ->
                val checked = checkedStates[habit.name] ?: false
                HabitItemCard(
                    habit = habit,
                    checked = checked,
                    onCheckedChange = { checkedStates[habit.name] = it }
                )
            }
        }
    }
}
