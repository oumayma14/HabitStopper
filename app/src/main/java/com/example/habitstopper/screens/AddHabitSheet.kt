package com.example.habitstopper.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// a list of colors the user can pick from for their habit card
val habitColors = listOf(
    "#FFF3E0", // orange tint
    "#FFEBEE", // red tint
    "#E3F2FD", // blue tint
    "#E8F5E9", // green tint
    "#F3E5F5", // purple tint
    "#FFF9C4"  // yellow tint
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitSheet(
    onDismiss: () -> Unit,
    onAdd: (name: String, iconName: String, colorHex: String) -> Unit
) {
    var habitName by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(habitColors[0]) }
    var errorText by remember { mutableStateOf<String?>(null) }

    // ModalBottomSheet slides up from the bottom of the screen
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "New Habit to Break",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(16.dp))

            // error message if name is empty
            if (errorText != null) {
                Text(
                    text = errorText!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(8.dp))
            }

            // habit name input
            OutlinedTextField(
                value = habitName,
                onValueChange = {
                    habitName = it
                    if (errorText != null) errorText = null
                },
                placeholder = { Text("e.g. No Sugar, No Smoking...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Pick a color",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(Modifier.height(8.dp))

            // color picker row
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                habitColors.forEach { colorHex ->
                    val color = Color(android.graphics.Color.parseColor(colorHex))
                    val isSelected = selectedColor == colorHex

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(color)
                            // show border if this color is selected
                            .then(
                                if (isSelected) Modifier.border(3.dp, Color.DarkGray, CircleShape)
                                else Modifier
                            )
                            .clickable { selectedColor = colorHex }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (habitName.trim().isEmpty()) {
                        errorText = "Please enter a habit name"
                        return@Button
                    }
                    // "default" is a placeholder for iconName
                    // we'll add icon picking later
                    onAdd(habitName.trim(), "default", selectedColor)
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Add Habit")
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}