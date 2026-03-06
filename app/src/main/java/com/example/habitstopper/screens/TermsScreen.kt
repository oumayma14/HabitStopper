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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TermsScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(colors = listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))))
                        .padding(horizontal = 24.dp)
                        .padding(top = 48.dp, bottom = 32.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                                .clickable { navController.popBackStack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.height(16.dp))
                        Text("Terms of Service", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface, letterSpacing = (-0.5).sp)
                        Spacer(Modifier.height(4.dp))
                        Text("Last updated: March 2025", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(24.dp)) {
                    TermsSection("1. Acceptance of Terms", "By using Quitly, you agree to these terms. If you do not agree, please do not use the app.")
                    TermsSection("2. Use of the App", "Quitly is a personal habit tracking app. You agree to use it only for lawful purposes and in a way that does not infringe the rights of others.")
                    TermsSection("3. Your Account", "You are responsible for maintaining the confidentiality of your account credentials. You are responsible for all activities that occur under your account.")
                    TermsSection("4. Data & Privacy", "We collect and process your data as described in our Privacy Policy. By using Quitly, you consent to such processing.")
                    TermsSection("5. Intellectual Property", "All content, features, and functionality of Quitly are owned by us and are protected by intellectual property laws.")
                    TermsSection("6. Termination", "We reserve the right to terminate or suspend your account at any time for any reason, including violation of these terms.")
                    TermsSection("7. Disclaimer", "Quitly is provided 'as is' without warranties of any kind. We do not guarantee that the app will be error-free or uninterrupted.")
                    TermsSection("8. Changes to Terms", "We may update these terms at any time. Continued use of the app after changes constitutes acceptance of the new terms.")
                    TermsSection("9. Contact", "For questions about these terms, please contact us through the feedback form in the app.")
                }
            }
        }
    }
}

@Composable
fun TermsSection(title: String, content: String) {
    Column(modifier = Modifier.padding(bottom = 20.dp)) {
        Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.height(6.dp))
        Text(content, fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f), lineHeight = 22.sp)
    }
}