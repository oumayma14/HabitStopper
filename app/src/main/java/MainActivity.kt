package com.example.habitstopper

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.habitstopper.ui.theme.HabitStopperTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HabitStopperTheme {

                // ---------- STREAK STATE ----------
                var streak by remember { mutableStateOf(1) }

                // ---------- DATE ----------
                val today = LocalDate.now()
                val dayName = today.format(DateTimeFormatter.ofPattern("EEEE"))
                val dayNumber = today.dayOfMonth
                val year = today.year

                fun getDaySuffix(day: Int): String {
                    return if (day in 11..13) {
                        "th"
                    } else {
                        when (day % 10) {
                            1 -> "st"
                            2 -> "nd"
                            3 -> "rd"
                            else -> "th"
                        }
                    }
                }

                val formattedSecondLine =
                    "$dayNumber${getDaySuffix(dayNumber)}, $year"

                // ---------- HEADER ----------
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 70.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {

                    // LEFT: Date
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = dayName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            text = formattedSecondLine,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Normal
                        )
                    }

                    // RIGHT: Streak (click to increase)
                    val streakColor =
                        if (streak > 0) Color(0xFFFF9800)
                        else MaterialTheme.colorScheme.onBackground

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            streak++ // tap to increase
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Whatshot,
                            contentDescription = "Streak",
                            tint = streakColor,
                            modifier = Modifier.size(50.dp)
                        )

                        Text(
                            text = streak.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Normal,
                            color = streakColor,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }


                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 170.dp, start = 10.dp, end = 24.dp)
                ) {
                    Text(
                        text = "What did you resist today?",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, start = 24.dp, end = 24.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
