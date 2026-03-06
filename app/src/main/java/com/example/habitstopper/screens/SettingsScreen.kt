package com.example.habitstopper.screens

import android.content.Intent
import androidx.core.net.toUri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.habitstopper.UserViewModel

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel()

    var darkMode by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var selectedLanguage by remember { mutableStateOf("English") }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val languages = listOf("English", "French", "Arabic", "Spanish", "German")

    val appVersion = "1.0.0"

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
                        .padding(horizontal = 24.dp)
                        .padding(top = 48.dp, bottom = 32.dp)
                ) {
                    Column {
                        Text(
                            text = "Settings",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Customize your experience",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.45f)
                        )
                    }
                }
            }

            // PREFERENCES SECTION
            item {
                Spacer(Modifier.height(24.dp))
                SettingsSectionLabel("PREFERENCES")
                Spacer(Modifier.height(10.dp))

                SettingsCard {
                    // Dark mode
                    SettingsToggleRow(
                        icon = Icons.Default.DarkMode,
                        iconGradient = listOf(Color(0xFF6C63FF), Color(0xFFA855F7)),
                        title = "Dark Mode",
                        subtitle = "Switch to dark theme",
                        checked = darkMode,
                        onCheckedChange = { darkMode = it }
                    )

                    SettingsDivider()

                    // Notifications
                    SettingsToggleRow(
                        icon = Icons.Default.Notifications,
                        iconGradient = listOf(Color(0xFFFF6B35), Color(0xFFFF4757)),
                        title = "Notifications",
                        subtitle = "Daily habit reminders",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )

                    SettingsDivider()

                    // Language
                    SettingsClickRow(
                        icon = Icons.Default.Language,
                        iconGradient = listOf(Color(0xFF00D4AA), Color(0xFF1E90FF)),
                        title = "Language",
                        subtitle = selectedLanguage,
                        onClick = { showLanguageDialog = true }
                    )
                }
            }

            // SUPPORT SECTION
            item {
                Spacer(Modifier.height(24.dp))
                SettingsSectionLabel("SUPPORT")
                Spacer(Modifier.height(10.dp))

                SettingsCard {
                    // Help center
                    SettingsClickRow(
                        icon = Icons.AutoMirrored.Filled.Help,
                        iconGradient = listOf(Color(0xFF6C63FF), Color(0xFF00D4AA)),
                        title = "Help Center / FAQ",
                        subtitle = "Browse common questions",
                        onClick = { navController.navigate("help") }
                    )

                    SettingsDivider()

                    // Send feedback
                    SettingsClickRow(
                        icon = Icons.Default.Feedback,
                        iconGradient = listOf(Color(0xFFFFD700), Color(0xFFFF6B35)),
                        title = "Send Feedback",
                        subtitle = "Report a bug or suggestion",
                        onClick = { navController.navigate("feedback") }

                    )
                }
            }

            // LEGAL SECTION
            item {
                Spacer(Modifier.height(24.dp))
                SettingsSectionLabel("LEGAL")
                Spacer(Modifier.height(10.dp))

                SettingsCard {
                    // Terms of service
                    SettingsClickRow(
                        icon = Icons.Default.Description,
                        iconGradient = listOf(Color(0xFF6C63FF), Color(0xFFA855F7)),
                        title = "Terms of Service",
                        subtitle = "Read our terms",
                        onClick = { navController.navigate("terms") }

                    )

                    SettingsDivider()

                    // Privacy policy
                    SettingsClickRow(
                        icon = Icons.Default.PrivacyTip,
                        iconGradient = listOf(Color(0xFF00D4AA), Color(0xFF6C63FF)),
                        title = "Privacy Policy",
                        subtitle = "How we handle your data",
                        onClick = { navController.navigate("privacy") }

                    )
                }
            }

            // APP VERSION
            item {
                Spacer(Modifier.height(24.dp))
                SettingsSectionLabel("ABOUT")
                Spacer(Modifier.height(10.dp))

                SettingsCard {
                    SettingsClickRow(
                        icon = Icons.Default.Info,
                        iconGradient = listOf(Color(0xFF6C63FF), Color(0xFF00D4AA)),
                        title = "App Version",
                        subtitle = "v$appVersion",
                        onClick = {}
                    )
                }
            }

            // DANGER ZONE
            item {
                Spacer(Modifier.height(24.dp))
                SettingsSectionLabel("DANGER ZONE")
                Spacer(Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFF4757).copy(alpha = 0.08f))
                        .border(1.dp, Color(0xFFFF4757).copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                        .clickable { showDeleteDialog = true }
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF4757).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteForever,
                                contentDescription = null,
                                tint = Color(0xFFFF4757),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Delete Account",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFFFF4757)
                            )
                            Text(
                                text = "Permanently delete your account and data",
                                fontSize = 12.sp,
                                color = Color(0xFFFF4757).copy(alpha = 0.6f)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }

    // LANGUAGE DIALOG
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            containerColor = Color(0xFF1A1A2E),
            shape = RoundedCornerShape(24.dp),
            title = {
                Text("Select Language", color = Color.White, fontWeight = FontWeight.ExtraBold)
            },
            text = {
                Column {
                    languages.forEach { language ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    selectedLanguage = language
                                    showLanguageDialog = false
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = language,
                                color = if (selectedLanguage == language) Color(0xFF00D4AA) else Color.White,
                                fontWeight = if (selectedLanguage == language) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 15.sp
                            )
                            if (selectedLanguage == language) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF00D4AA),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }

    // DELETE ACCOUNT DIALOG
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = Color(0xFF1A1A2E),
            shape = RoundedCornerShape(24.dp),
            title = {
                Text("Delete Account", color = Color(0xFFFF4757), fontWeight = FontWeight.ExtraBold)
            },
            text = {
                Text(
                    "This will permanently delete your account and all your habits. This action cannot be undone.",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            },
            confirmButton = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFF4757))
                        .clickable {
                            showDeleteDialog = false
                            userViewModel.deleteAccount {
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .clickable { showDeleteDialog = false }
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text("Cancel", color = Color.White, fontWeight = FontWeight.Medium)
                }
            }
        )
    }
}

// REUSABLE COMPOSABLES

@Composable
fun SettingsSectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFF1A1A2E).copy(alpha = 0.4f),
        letterSpacing = 2.sp,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
}

@Composable
fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
    ) {
        content()
    }
}

@Composable
fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 20.dp),
        color = Color(0xFF1A1A2E).copy(alpha = 0.06f)
    )
}

@Composable
fun SettingsToggleRow(
    icon: ImageVector,
    iconGradient: List<Color>,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(colors = iconGradient)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color(0xFF1A1A2E))
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF6C63FF),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}

@Composable
fun SettingsClickRow(
    icon: ImageVector,
    iconGradient: List<Color>,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(colors = iconGradient)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color(0xFF1A1A2E))
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(20.dp)
        )
    }
}