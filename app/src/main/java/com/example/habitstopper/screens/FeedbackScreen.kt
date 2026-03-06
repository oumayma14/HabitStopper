package com.example.habitstopper.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.navigation.NavController

@Composable
fun FeedbackScreen(navController: NavController) {
    var selectedType by remember { mutableStateOf("Bug Report") }
    var message by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    val feedbackTypes = listOf("Bug Report", "Feature Request", "General Feedback", "Other")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E))
                        )
                    )
                    .padding(horizontal = 24.dp)
                    .padding(top = 48.dp, bottom = 32.dp)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                            .clickable { navController.popBackStack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Text("Send Feedback", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = (-0.5).sp)
                    Spacer(Modifier.height(4.dp))
                    Text("We read every message", fontSize = 14.sp, color = Color.White.copy(alpha = 0.45f))
                }
            }

            if (submitted) {
                // SUCCESS STATE
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(40.dp))
                    Text("🎉", fontSize = 56.sp)
                    Spacer(Modifier.height(16.dp))
                    Text("Thanks for your feedback!", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(8.dp))
                    Text("We'll review it and get back to you if needed.", fontSize = 14.sp, color = Color.Gray, lineHeight = 22.sp)
                    Spacer(Modifier.height(32.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.horizontalGradient(colors = listOf(Color(0xFF6C63FF), Color(0xFF00D4AA))))
                            .clickable { navController.popBackStack() }
                            .padding(horizontal = 32.dp, vertical = 14.dp)
                    ) {
                        Text("Go Back", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                    }
                }
            } else {
                Column(modifier = Modifier.padding(24.dp)) {
                    Spacer(Modifier.height(8.dp))

                    // FEEDBACK TYPE
                    Text("FEEDBACK TYPE", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f), letterSpacing = 2.sp)
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        feedbackTypes.take(2).forEach { type ->
                            val selected = selectedType == type
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (selected) Brush.horizontalGradient(colors = listOf(Color(0xFF6C63FF), Color(0xFFA855F7)))
                                        else Brush.horizontalGradient(colors = listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surface))
                                    )
                                    .clickable { selectedType = type }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(type, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = if (selected) Color.White else Color.Gray)
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        feedbackTypes.drop(2).forEach { type ->
                            val selected = selectedType == type
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (selected) Brush.horizontalGradient(colors = listOf(Color(0xFF6C63FF), Color(0xFFA855F7)))
                                        else Brush.horizontalGradient(colors = listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surface))
                                    )
                                    .clickable { selectedType = type }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(type, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = if (selected) Color.White else Color.Gray)
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // MESSAGE
                    Text("MESSAGE", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f), letterSpacing = 2.sp)
                    Spacer(Modifier.height(10.dp))
                    TextField(
                        value = message,
                        onValueChange = { message = it },
                        placeholder = { Text("Describe your issue or suggestion...", color = Color.Gray, fontSize = 14.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            cursorColor = Color(0xFF6C63FF)
                        )
                    )

                    Spacer(Modifier.height(28.dp))

                    // SUBMIT BUTTON
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (message.isNotBlank())
                                    Brush.horizontalGradient(colors = listOf(Color(0xFF6C63FF), Color(0xFF00D4AA)))
                                else
                                    Brush.horizontalGradient(colors = listOf(Color.LightGray, Color.LightGray))
                            )
                            .clickable(enabled = message.isNotBlank()) { submitted = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Submit Feedback", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}