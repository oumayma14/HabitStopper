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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val habitColors = listOf(
    "#FF4757", // vivid red
    "#FF6B35", // vivid orange
    "#FFD700", // vivid yellow
    "#2ED573", // vivid green
    "#1E90FF", // vivid blue
    "#A855F7", // vivid purple
    "#FF3CAC", // vivid pink
    "#00D4AA"  // vivid teal
)

val habitEmojis = listOf(
    "🚬", "🍬", "🍷", "📱", "🎮",
    "☕", "🍔", "😤", "💸", "🌙",
    "🍕", "🧁", "🥤", "😴", "🛒"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitSheet(
    onDismiss: () -> Unit,
    onAdd: (name: String, iconName: String, colorHex: String) -> Unit
) {
    var habitName by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(habitColors[0]) }
    var selectedEmoji by remember { mutableStateOf(habitEmojis[0]) }
    var errorText by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // colorful drag handle
            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFFFF4757), Color(0xFFA855F7))
                        )
                    )
            )

            Spacer(Modifier.height(24.dp))

            // bold title with gradient text effect
            Text(
                text = "💥 Break a Habit",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "What are you quitting today?",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
            )

            Spacer(Modifier.height(28.dp))

            // INPUT FIELD with colored left border
            val selectedColorParsed = remember(selectedColor) {
                try { Color(android.graphics.Color.parseColor(selectedColor)) }
                catch (e: Exception) { Color(0xFFFF4757) }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(
                        width = 2.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(selectedColorParsed, Color(0xFFA855F7))
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedEmoji,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
                TextField(
                    value = habitName,
                    onValueChange = {
                        habitName = it
                        if (errorText != null) errorText = null
                    },
                    placeholder = {
                        Text(
                            "e.g. No Sugar, No Smoking...",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                            fontSize = 14.sp
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = selectedColorParsed
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            if (errorText != null) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "⚠️ $errorText",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(28.dp))

            // EMOJI PICKER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionLabel("Choose Emoji")
            }

            Spacer(Modifier.height(12.dp))

            habitEmojis.chunked(5).forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row.forEach { emoji ->
                        val isSelected = selectedEmoji == emoji
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isSelected)
                                        Brush.linearGradient(
                                            colors = listOf(
                                                selectedColorParsed.copy(alpha = 0.4f),
                                                Color(0xFFA855F7).copy(alpha = 0.4f)
                                            )
                                        )
                                    else
                                        Brush.linearGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                                            )
                                        )
                                )
                                .then(
                                    if (isSelected) Modifier.border(
                                        2.dp,
                                        Brush.linearGradient(
                                            colors = listOf(selectedColorParsed, Color(0xFFA855F7))
                                        ),
                                        RoundedCornerShape(14.dp)
                                    ) else Modifier
                                )
                                .clickable { selectedEmoji = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(emoji, fontSize = 26.sp)
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(24.dp))

            // COLOR PICKER
            SectionLabel("Choose Color")
            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
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
                            .then(
                                if (isSelected) Modifier.border(3.dp, Color.White, CircleShape)
                                else Modifier.border(
                                    1.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f), CircleShape
                                )
                            )
                            .clickable { selectedColor = colorHex },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Text(
                                "✓",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // GRADIENT ADD BUTTON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                selectedColorParsed,
                                Color(0xFFA855F7)
                            )
                        )
                    )
                    .clickable {
                        if (habitName.trim().isEmpty()) {
                            errorText = "Please enter a habit name"
                            return@clickable
                        }
                        onAdd(habitName.trim(), selectedEmoji, selectedColor)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add Habit 🚀",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
        letterSpacing = 2.sp,
        modifier = Modifier.fillMaxWidth()
    )
}