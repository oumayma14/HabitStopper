package com.example.habitstopper.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitstopper.navigation.BottomNavItem
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.habitstopper.R
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.platform.LocalContext
import com.example.habitstopper.com.example.habitstopper.GoogleAuthClient
import com.example.habitstopper.com.example.habitstopper.auth.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) } //loading state
    var errorText by remember { mutableStateOf<String?>(null) } //error msg for validation
    val panelColor= Color(0xFF4F6D7A)
    val pageBg= Color.White
    val viewModel : AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val googleAuthClient = remember { GoogleAuthClient(context) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        result ->
        if (result.resultCode == Activity.RESULT_OK){
            result.data?.let {
                intent ->
                scope.launch {
                    isLoading = true
                    val signInResult = googleAuthClient.signInWithIntent(intent)
                    isLoading = false
                    if (signInResult.isSuccess) {
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        errorText = "Google Sign-In failed. Try again."
                    }
                }
            }

        }
    }

    //client-side validation function
    fun validate(): Boolean {
        val trimmedEmail = email.trim()
        if(trimmedEmail.isEmpty()){
            errorText = "Email is required"
            return false
        }

        if(!trimmedEmail.contains("@") || !trimmedEmail.contains(".")) {
            errorText = "Enter a valid email."
            return false
        }

        if (password.length<6){
            errorText = "Password must be at least 6 characters"
            return false
        }
        errorText = null
        return true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(pageBg)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 52.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_quitly),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(0.9f)
                    .padding(horizontal = 24.dp),
                contentScale = ContentScale.Fit

            )
        }
        //bottom rounded panel
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.72f)
                .align(Alignment.BottomCenter),
            color = panelColor,
            shape = RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp),
            tonalElevation = 0.dp,
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Already a user?",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Spacer(Modifier.height(24.dp))
                if (errorText != null) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = errorText!!,
                        color = Color(0xFFFFD6D6),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                //input 1
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it
                        if (errorText != null) errorText = null
                    },
                    placeholder = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF3ECEC),
                        unfocusedContainerColor = Color(0xFFF3ECEC),

                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,

                        cursorColor = panelColor,

                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                Spacer(Modifier.height(16.dp))

                //input 2
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it
                        if (errorText != null) errorText = null
                    },
                    placeholder = { Text("Password") },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = Color(0xFF2F2F2F)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF3ECEC),
                        unfocusedContainerColor = Color(0xFFF3ECEC),

                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,

                        cursorColor = panelColor,

                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                Spacer(Modifier.height(16.dp))

                //forget password placeholder
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Forgot password?",
                        color = Color.White.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable(enabled = !isLoading) {
                                // later: navController.navigate("forgot")
                            }
                    )
                }
                Spacer(Modifier.height(16.dp))

                //login button
                Button(
                    onClick = {
                        if (!validate()) return@Button

                        viewModel.login(email,password){
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo("login") { inclusive = true }
                        }}
                    },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE7E7E7),
                        contentColor = Color(0xFF2A2A2A)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .width(120.dp)
                        .height(42.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp),
                            color = Color(0xFF2A2A2A)
                        )
                    } else {
                        Text("Log In")
                    }
                }
                Spacer(Modifier.height(18.dp))

                //placeholder or
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.dp,
                        color = Color.White.copy(alpha = 0.35f)
                    )
                    Text(
                        text = "Or",
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.dp,
                        color = Color.White.copy(alpha = 0.35f)
                    )
                }
                Spacer(Modifier.height(16.dp))
                // google button
                Button(
                    onClick = { launcher.launch(googleAuthClient.getSignInIntent()) },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF3ECEC),
                        contentColor = Color(0xFF1F1F1F)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = "Google",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Sign in with Google")
                    }
                }
                Spacer(Modifier.height(16.dp))

                //sign up text
                Text(
                    text = "New to Quitly?",
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Create an account",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable {
                        navController.navigate("signup")
                    }
                )
            }
        }
    }

}