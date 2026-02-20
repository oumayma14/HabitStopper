package com.example.habitstopper

import android.R
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.habitstopper.ui.theme.HabitStopperTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector


data class HabitCard(
    val name: String,
    val icon: ImageVector,
    val cardColor: Color
)
val habits = listOf(
    HabitCard("No Sugar", Icons.Filled.LocalCafe, Color(0xFFFFF3E0)),
    HabitCard("No Smoking", Icons.Filled.Whatshot, Color(0xFFFFEBEE)),
    HabitCard("No DoomScrolling", Icons.Filled.Smartphone, Color(0xFFE3F2FD))
)

@Composable
fun HabitItemCard(
    habit: HabitCard,
    checked: Boolean,
    onCheckedChange:(Boolean)-> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor=habit.cardColor
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = habit.icon,
                    contentDescription = habit.name,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(habit.name)
            }

            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}
    class MainActivity : ComponentActivity() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()

            setContent {
                HabitStopperTheme {
                    val checkedStates = remember { mutableStateMapOf<String, Boolean>() }

                    // ---------- STREAK STATE ----------
                    var streak by remember { mutableStateOf(0) }

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

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 40.dp)
                    ) {
                        //HEADER (CONTAINS DATE AND STREAK)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            //DATE COLUMN
                            Column {
                                Text(dayName, style = MaterialTheme.typography.headlineSmall)
                                Text(
                                    formattedSecondLine,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                            //STREAK ROW
                            val streakColor =
                                if (streak > 0) Color(0xFFFF9800)
                                else MaterialTheme.colorScheme.onBackground
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
                        //TITLE
                        Text(
                            text = "What did you resist today ?",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        //+ ADD HABIT BUTTON

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

                        //CARDS
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
            }
        }
    }