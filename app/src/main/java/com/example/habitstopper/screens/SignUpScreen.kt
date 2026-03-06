package com.example.habitstopper.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.habitstopper.GoogleAuthClient
import com.example.habitstopper.R
import com.example.habitstopper.auth.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(navController: NavController) {
    var displayName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }
    val viewModel: AuthViewModel = viewModel()
    val isLoading = viewModel.isLoading

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val googleAuthClient = remember { GoogleAuthClient(context) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                scope.launch {
                    val signInResult = googleAuthClient.signInWithIntent(intent)
                    if (signInResult.isSuccess) {
                        navController.navigate("home") {
                            popUpTo("signup") { inclusive = true }
                        }
                    }
                }
            }
        }
    }

    fun validate(): Boolean {
        if (displayName.trim().isEmpty()) { errorText = "Name is required!"; return false }
        val trimmedEmail = email.trim()
        if (trimmedEmail.isEmpty()) { errorText = "Email is required!"; return false }
        if (!trimmedEmail.contains("@") || !trimmedEmail.contains(".")) { errorText = "Enter a valid email."; return false }
        if (password.length < 6) { errorText = "Password must be at least 6 characters"; return false }
        if (confirmPassword != password) { errorText = "Passwords do not match"; return false }
        errorText = null
        return true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.surface)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(64.dp))

            // LOGO
            Image(
                painter = painterResource(id = R.drawable.logo_quitly),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxWidth(0.5f),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Join Quitly 🚀",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = (-0.5).sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Start breaking bad habits today",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
            )

            Spacer(Modifier.height(36.dp))

            // ERROR MESSAGES
            if (errorText != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.15f))
                        .border(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text("⚠️ $errorText", color = MaterialTheme.colorScheme.error, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(Modifier.height(16.dp))
            }
            viewModel.errorMessage?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.15f))
                        .border(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text("⚠️ $it", color = MaterialTheme.colorScheme.error, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(Modifier.height(16.dp))
            }

            // FIELDS
            AuthTextField(
                value = displayName,
                onValueChange = { displayName = it; if (errorText != null) errorText = null },
                placeholder = "Your name"
            )
            Spacer(Modifier.height(14.dp))
            AuthTextField(
                value = email,
                onValueChange = { email = it; if (errorText != null) errorText = null },
                placeholder = "Email"
            )
            Spacer(Modifier.height(14.dp))
            AuthTextField(
                value = password,
                onValueChange = { password = it; if (errorText != null) errorText = null },
                placeholder = "Create a password",
                isPassword = true,
                showPassword = showPassword,
                onTogglePassword = { showPassword = !showPassword }
            )
            Spacer(Modifier.height(14.dp))
            AuthTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; if (errorText != null) errorText = null },
                placeholder = "Confirm password",
                isPassword = true,
                showPassword = showPassword,
                onTogglePassword = { showPassword = !showPassword }
            )

            Spacer(Modifier.height(32.dp))

            // SIGN UP BUTTON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF6C63FF), Color(0xFF00D4AA))
                        )
                    )
                    .clickable(enabled = !isLoading) {
                        if (!validate()) return@clickable
                        viewModel.signUp(email, password, displayName) {
                            navController.navigate("login") {
                                popUpTo("signup") { inclusive = true }
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(22.dp), color = Color.White)
                } else {
                    Text("Create Account", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                }
            }

            Spacer(Modifier.height(24.dp))

            // OR DIVIDER
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                Text("  Or continue with  ", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.35f), fontSize = 12.sp)
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
            }

            Spacer(Modifier.height(24.dp))

            // GOOGLE BUTTON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                    .clickable(enabled = !isLoading) {
                        launcher.launch(googleAuthClient.getSignInIntent())
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Image(painter = painterResource(id = R.drawable.google), contentDescription = "Google", modifier = Modifier.size(22.dp))
                    Spacer(Modifier.width(12.dp))
                    Text("Continue with Google", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                }
            }

            Spacer(Modifier.height(32.dp))

            // LOGIN LINK
            Row {
                Text("Already have an account? ", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f), fontSize = 14.sp)
                Text(
                    text = "Log in",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.clickable { navController.navigate("login") }
                )
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}