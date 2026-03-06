package com.example.habitstopper.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.habitstopper.HabitViewModel
import com.example.habitstopper.UserViewModel
import java.text.SimpleDateFormat
import java.util.*

data class Badge(
    val emoji: String,
    val name: String,
    val requiredStreak: Int,
    val gradient: List<Color>
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(navController: NavController) {
    val userViewModel: UserViewModel = viewModel()
    val habitViewModel: HabitViewModel = viewModel()
    var showEditSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        userViewModel.loadUserProfile()
        habitViewModel.loadHabits()
    }

    val userProfile = userViewModel.userProfile
    val habits = habitViewModel.habits
    val bestStreak = habits.maxOfOrNull { it.streak } ?: 0
    val bestStreakEver = maxOf(bestStreak, userProfile?.bestStreakEver ?: 0)
    val totalHabits = habits.size
    val completedToday = habits.count { it.checkedToday }

    val joinDate = remember(userProfile?.joinDate) {
        userProfile?.joinDate?.let {
            SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date(it))
        } ?: "—"
    }

    val badges = listOf(
        Badge("🌱", "Beginner", 1, listOf(Color(0xFF56AB2F), Color(0xFFA8E063))),
        Badge("🔥", "On Fire", 3, listOf(Color(0xFFFF6B6B), Color(0xFFFF4757))),
        Badge("💧", "Consistent", 5, listOf(Color(0xFF1E90FF), Color(0xFF00D4AA))),
        Badge("⚡", "Energized", 7, listOf(Color(0xFFFFD700), Color(0xFFFFA500))),
        Badge("💪", "Committed", 14, listOf(Color(0xFF6C63FF), Color(0xFFA855F7))),
        Badge("🧠", "Mindful", 21, listOf(Color(0xFF00D4AA), Color(0xFF1E90FF))),
        Badge("🦁", "Fierce", 30, listOf(Color(0xFFFF6B35), Color(0xFFFF4757))),
        Badge("💎", "Diamond", 50, listOf(Color(0xFF00D4AA), Color(0xFF6C63FF))),
        Badge("🚀", "Rocket", 75, listOf(Color(0xFFA855F7), Color(0xFF6C63FF))),
        Badge("👑", "Legend", 100, listOf(Color(0xFFFFD700), Color(0xFFFF6B35)))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                                colors = listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                            )
                        )
                        .padding(horizontal = 24.dp)
                        .padding(top = 48.dp, bottom = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (!userProfile?.photoUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                                    .data(userProfile?.photoUrl)
                                    .diskCachePolicy(coil.request.CachePolicy.DISABLED)
                                    .memoryCachePolicy(coil.request.CachePolicy.DISABLED)
                                    .build(),
                                contentDescription = "Profile Photo",
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .clickable { showEditSheet = true }
                                    .border(
                                        3.dp,
                                        Brush.linearGradient(
                                            colors = listOf(Color(0xFF6C63FF), Color(0xFF00D4AA))
                                        ),
                                        CircleShape
                                    )
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(Color(0xFF6C63FF), Color(0xFFA855F7))
                                        )
                                    )
                                    .border(
                                        3.dp,
                                        Brush.linearGradient(
                                            colors = listOf(Color(0xFF00D4AA), Color(0xFF6C63FF))
                                        ),
                                        CircleShape
                                    )
                                    .clickable { showEditSheet = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = userProfile?.displayName?.take(1)?.uppercase() ?: "?",
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = userProfile?.displayName ?: "Loading...",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "Member since $joinDate",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )

                        Spacer(Modifier.height(12.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                                .clickable { showEditSheet = true }
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "✏️ Edit Profile",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // STATS
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProfileStatCard("🎯", totalHabits.toString(), "Total Habits", listOf(Color(0xFF6C63FF), Color(0xFFA855F7)), Modifier.weight(1f))
                    ProfileStatCard("✅", completedToday.toString(), "Done Today", listOf(Color(0xFF00D4AA), Color(0xFF6C63FF)), Modifier.weight(1f))
                    ProfileStatCard("🔥", bestStreakEver.toString(), "Best Streak", listOf(Color(0xFFFF6B6B), Color(0xFFFF4757)), Modifier.weight(1f))
                }
            }

            // BADGES
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "STREAK BADGES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                        letterSpacing = 2.sp
                    )

                    val nextBadge = badges.firstOrNull { bestStreakEver < it.requiredStreak }
                    val prevBadge = badges.lastOrNull { bestStreakEver >= it.requiredStreak }

                    if (nextBadge != null) {
                        val progressStart = prevBadge?.requiredStreak ?: 0
                        val progressEnd = nextBadge.requiredStreak
                        val progress = ((bestStreakEver - progressStart).toFloat() / (progressEnd - progressStart)).coerceIn(0f, 1f)
                        Spacer(Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Next: ${nextBadge.emoji} ${nextBadge.name}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                            Text("$bestStreakEver / ${nextBadge.requiredStreak} days", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                        }

                        Spacer(Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Brush.horizontalGradient(colors = nextBadge.gradient))
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                    } else {
                        Spacer(Modifier.height(10.dp))
                        Text("🎉 All badges unlocked! You're a Legend!", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                        Spacer(Modifier.height(16.dp))
                    }

                    badges.chunked(5).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                            row.forEach { badge ->
                                BadgeItem(badge = badge, unlocked = (userProfile?.unlockedBadges ?: emptyList()).contains(badge.name) || bestStreakEver >= badge.requiredStreak, modifier = Modifier.weight(1f))                            }
                        }
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }

            // HABITS LIST
            item {
                Spacer(Modifier.height(24.dp))
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                    Text("MY HABITS", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f), letterSpacing = 2.sp)
                    Spacer(Modifier.height(12.dp))
                    if (habits.isEmpty()) {
                        Text("No habits yet — go add some!", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), fontSize = 14.sp)
                    }
                    habits.forEach { habit ->
                        val cardColor = remember(habit.colorHex) {
                            try { Color(android.graphics.Color.parseColor(habit.colorHex)) }
                            catch (e: Exception) { Color(0xFF6C63FF) }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Brush.linearGradient(colors = listOf(cardColor, Color(0xFFA855F7))))
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${habit.iconName} ${habit.name}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("🔥 ${habit.streak}", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }

            // SIGN OUT
            item {
                Spacer(Modifier.height(16.dp))
                val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.12f))
                        .border(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .clickable {
                            auth.signOut()
                            navController.navigate("login") { popUpTo(0) { inclusive = true } }
                        }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sign Out", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Spacer(Modifier.height(24.dp))
            }
        }

        if (showEditSheet) {
            EditProfileSheet(
                currentName = userProfile?.displayName ?: "",
                currentPhotoURL = userProfile?.photoUrl ?: "",
                onDismiss = { showEditSheet = false },
                userViewModel = userViewModel
            )
        }

        //sync best streak and badges to firestore
        LaunchedEffect(habits) {
            if (habits.isEmpty()) return@LaunchedEffect
            val currentBestStreak = habits.maxOfOrNull { it.streak } ?: 0
            val unlockedBadgeNames = badges
                .filter { bestStreakEver >= it.requiredStreak }
                .map { it.name }
            userViewModel.syncStreakAndBadges(bestStreakEver, unlockedBadgeNames)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileSheet(
    currentName: String,
    currentPhotoURL: String,
    onDismiss: () -> Unit,
    userViewModel: UserViewModel
) {
    var displayName by remember { mutableStateOf(currentName) }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }
    var showPhotoTip by remember { mutableStateOf(false) }
    var photoUrl by remember { mutableStateOf(currentPhotoURL) }



    val successMessage = userViewModel.updateSuccess
    val errorMessage = userViewModel.updateError


    LaunchedEffect(successMessage, errorMessage) {
        if (successMessage != null || errorMessage != null) {
            kotlinx.coroutines.delay(2000)
            userViewModel.clearMessage()
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            userViewModel.clearMessage()
            onDismiss()
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // drag handle
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(Brush.horizontalGradient(colors = listOf(Color(0xFF6C63FF), Color(0xFFA855F7))))
            )

            Spacer(Modifier.height(20.dp))
            Text("Edit Profile", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.height(24.dp))

            // SUCCESS
            if (successMessage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f))
                        .border(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text("✅ $successMessage", color = MaterialTheme.colorScheme.secondary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(Modifier.height(16.dp))
            }

            // ERROR
            if (errorText != null || errorMessage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.15f))
                        .border(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text("⚠️ ${errorText ?: errorMessage}", color = MaterialTheme.colorScheme.error, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(Modifier.height(16.dp))
            }

            // DISPLAY NAME
            SectionLabel("Display Name")
            Spacer(Modifier.height(10.dp))
            AuthTextField(value = displayName, onValueChange = { displayName = it; errorText = null }, placeholder = "Your name")
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.horizontalGradient(colors = listOf(Color(0xFF6C63FF), Color(0xFFA855F7))))
                    .clickable {
                        if (displayName.trim().isEmpty()) { errorText = "Name cannot be empty"; return@clickable }
                        userViewModel.updateDisplayName(displayName.trim())
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Update Name", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(Modifier.height(28.dp))

            // PROFILE PHOTO
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "PROFILE PHOTO URL",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                    letterSpacing = 2.sp
                )
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f))
                        .clickable { showPhotoTip = !showPhotoTip },
                    contentAlignment = Alignment.Center
                ) {
                    Text("?", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
                }
            }
            if (showPhotoTip) {
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "1. Go to imgur.com\n2. Upload your photo\n3. Right-click the image → Copy image address\n4. Paste the link here and tap Update Photo",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                        lineHeight = 20.sp
                    )
                }
            }
            Spacer(Modifier.height(6.dp))

            AuthTextField(
                value = photoUrl,
                onValueChange = { photoUrl = it; errorText = null },
                placeholder = "https://i.imgur.com/..."
            )
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF6C63FF), Color(0xFF00D4AA))
                        )
                    )
                    .clickable {
                        if (photoUrl.trim().isEmpty()) {
                            errorText = "Please enter a photo URL"
                            return@clickable
                        }
                        userViewModel.updatePhotoUrl(photoUrl.trim())
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Update Photo", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(Modifier.height(28.dp))

            // PASSWORD
            SectionLabel("Change Password")
            Spacer(Modifier.height(10.dp))
            AuthTextField(value = newPassword, onValueChange = { newPassword = it; errorText = null }, placeholder = "New password", isPassword = true, showPassword = showPassword, onTogglePassword = { showPassword = !showPassword })
            Spacer(Modifier.height(10.dp))
            AuthTextField(value = confirmPassword, onValueChange = { confirmPassword = it; errorText = null }, placeholder = "Confirm new password", isPassword = true, showPassword = showPassword, onTogglePassword = { showPassword = !showPassword })
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.horizontalGradient(colors = listOf(Color(0xFFFF6B35), Color(0xFFFF4757))))
                    .clickable {
                        if (newPassword.length < 6) { errorText = "Password must be at least 6 characters"; return@clickable }
                        if (newPassword != confirmPassword) { errorText = "Passwords do not match"; return@clickable }
                        userViewModel.updatePassword(newPassword)
                        newPassword = ""
                        confirmPassword = ""
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Update Password", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun ProfileStatCard(emoji: String, value: String, label: String, gradient: List<Color>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(colors = gradient))
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 22.sp)
        Spacer(Modifier.height(6.dp))
        Text(value, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
        Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f), textAlign = TextAlign.Center)
    }
}

@Composable
fun BadgeItem(badge: Badge, unlocked: Boolean, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(1000, easing = EaseInOut), repeatMode = RepeatMode.Reverse),
        label = "glowAlpha"
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.05f,
        animationSpec = infiniteRepeatable(animation = tween(1000, easing = EaseInOut), repeatMode = RepeatMode.Reverse),
        label = "scale"
    )

    Column(
        modifier = modifier
            .graphicsLayer { scaleX = if (unlocked) scale else 1f; scaleY = if (unlocked) scale else 1f }
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (unlocked) Brush.linearGradient(colors = badge.gradient)
                else Brush.linearGradient(colors = listOf(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f), MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)))
            )
            .then(
                if (unlocked) Modifier.border(2.dp, Brush.linearGradient(colors = badge.gradient.map { it.copy(alpha = glowAlpha) }), RoundedCornerShape(14.dp))
                else Modifier
            )
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(if (unlocked) badge.emoji else "🔒", fontSize = 22.sp)
        Spacer(Modifier.height(4.dp))
        Text(badge.name, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (unlocked) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), textAlign = TextAlign.Center, maxLines = 1)
        Text("${badge.requiredStreak}d", fontSize = 9.sp, color = if (unlocked) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f), textAlign = TextAlign.Center)
    }
}