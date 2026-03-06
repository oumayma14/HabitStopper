package com.example.habitstopper.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
fun PrivacyScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F4F0))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E))))
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
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.height(16.dp))
                        Text("Privacy Policy", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = (-0.5).sp)
                        Spacer(Modifier.height(4.dp))
                        Text("Last updated: March 2025", fontSize = 14.sp, color = Color.White.copy(alpha = 0.45f))
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(24.dp)) {
                    TermsSection("1. Information We Collect", "We collect information you provide directly, such as your name, email address, and profile photo URL. We also collect usage data such as habits you create and your streak history.")
                    TermsSection("2. How We Use Your Information", "We use your information to provide and improve the app, personalize your experience, and communicate with you about updates or issues.")
                    TermsSection("3. Data Storage", "Your data is stored securely using Google Firebase. We use industry-standard security measures to protect your information.")
                    TermsSection("4. Data Sharing", "We do not sell your personal data to third parties. We may share data with service providers who help us operate the app, such as Firebase.")
                    TermsSection("5. Your Rights", "You can access, update, or delete your account and data at any time through the app. Deleting your account will permanently remove all your data.")
                    TermsSection("6. Cookies & Tracking", "We do not use cookies. We may collect anonymous analytics data to improve the app experience.")
                    TermsSection("7. Children's Privacy", "Quitly is not intended for children under 13. We do not knowingly collect data from children under 13.")
                    TermsSection("8. Changes to This Policy", "We may update this policy from time to time. We will notify you of significant changes through the app.")
                    TermsSection("9. Contact Us", "If you have questions about this privacy policy, please contact us through the feedback form in the app.")
                }
            }
        }
    }
}