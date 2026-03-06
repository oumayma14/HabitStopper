package com.example.habitstopper.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

data class FaqItem(val question: String, val answer: String)

@Composable
fun HelpScreen(navController: NavController) {
    val faqs = listOf(
        FaqItem("How does the streak work?", "Your streak increases by 1 every day you check a habit. If you miss a day, your streak resets to 0. Make sure to check your habits every day to keep your streak alive!"),
        FaqItem("What happens if I don't check a habit for 24 hours?", "If you don't check a habit within 24 hours, your streak resets to 0. However your all-time best streak is always saved and never decreases."),
        FaqItem("How do I add a new habit?", "On the Home screen, tap the 'New Habit' button in the top right. Choose a name, an emoji icon, and a color for your habit."),
        FaqItem("How do I delete a habit?", "On the Home screen, tap the trash icon on any habit card to delete it permanently."),
        FaqItem("How do I earn badges?", "Badges are earned by reaching streak milestones. For example, reach a 3-day streak to earn the 🔥 On Fire badge. Once earned, badges are yours forever."),
        FaqItem("Can I change my profile photo?", "Yes! Go to Profile → Edit Profile → Profile Photo URL. Upload a photo to imgur.com and paste the direct image link."),
        FaqItem("How do I change my password?", "Go to Profile → Edit Profile → Change Password. Enter your new password twice and tap Update Password."),
        FaqItem("How do I delete my account?", "Go to Settings → Delete Account. This will permanently delete your account and all your habits. This action cannot be undone."),
        FaqItem("Is my data saved if I log out?", "Yes! All your habits, streaks, and badges are saved to the cloud. You can log back in on any device and your data will be there."),
        FaqItem("How do I contact support?", "Go to Settings → Send Feedback and fill out the form. We'll get back to you as soon as possible.")
    )

    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F4F0))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 40.dp)
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
                        Text("Help Center", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = (-0.5).sp)
                        Spacer(Modifier.height(4.dp))
                        Text("Frequently asked questions", fontSize = 14.sp, color = Color.White.copy(alpha = 0.45f))
                    }
                }
            }

            // FAQ ITEMS
            item { Spacer(Modifier.height(24.dp)) }

            faqs.forEachIndexed { index, faq ->
                item {
                    val isExpanded = expandedIndex == index
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .clickable {
                                expandedIndex = if (isExpanded) null else index
                            }
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = faq.question,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Color(0xFF1A1A2E),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = if (isExpanded) "▲" else "▼",
                                fontSize = 12.sp,
                                color = Color(0xFF6C63FF)
                            )
                        }
                        if (isExpanded) {
                            Spacer(Modifier.height(10.dp))
                            HorizontalDivider(color = Color(0xFF1A1A2E).copy(alpha = 0.06f))
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = faq.answer,
                                fontSize = 13.sp,
                                color = Color.Gray,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}