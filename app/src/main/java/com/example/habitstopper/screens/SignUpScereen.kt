package com.example.habitstopper.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitstopper.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.example.habitstopper.GoogleAuthClient
import com.example.habitstopper.auth.AuthViewModel

@Composable
fun SignUpScreen(navController: NavController) {
    val panelColor= Color(0xFF4F6D7A)
    var errorText by remember { mutableStateOf<String?>(null) } //error msg for validation
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    val viewModel : AuthViewModel = viewModel()
    val isLoading = viewModel.isLoading //loading state

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
        val trimmedEmail = email.trim()
        if(trimmedEmail.isEmpty()){
            errorText = "Email is required!"
            return false
        }

        if(!trimmedEmail.contains("@") || !trimmedEmail.contains(".")) {
            errorText = "Enter a valid email."
            return false
        }

        if (password.length < 6) {
            errorText = "Password must be at least 6 characters"
            return false
        }
        if (confirmPassword != password) {
            errorText = "Passwords do not match"
            return false
        }
        errorText = null
        return true
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(panelColor)
            .padding(horizontal = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create an account",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

            Spacer(Modifier.height(22.dp))
            if (errorText != null) {
                Text(
                    text = errorText!!,
                    color = Color(0xFFFFD6D6),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(10.dp))
            }
            viewModel.errorMessage?.let { firebaseError ->
                Text(
                    text = firebaseError,
                    color = Color(0xFFFFD6D6),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(10.dp))
            }
            val fieldBg = Color(0xFFF3ECEC)
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (errorText != null) errorText = null
                },
                placeholder = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.92f),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = fieldBg,
                    unfocusedContainerColor = fieldBg,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = panelColor,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
            Spacer(Modifier.height(14.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (errorText != null) errorText = null
                },
                placeholder = { Text("Create a password") },
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
                modifier = Modifier.fillMaxWidth(0.92f),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = fieldBg,
                    unfocusedContainerColor = fieldBg,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = panelColor,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    if (errorText != null) errorText = null
                },
                placeholder = { Text("Type your password again") },
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
                modifier = Modifier.fillMaxWidth(0.92f),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = fieldBg,
                    unfocusedContainerColor = fieldBg,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = panelColor,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
            Spacer(Modifier.height(22.dp))

            val signUpBtn = Color(0xFFBFD3DD)

            Button(
                onClick = {
                    if (!validate()) return@Button
                    viewModel.signUp(email, password) {
                        navController.navigate("login") {
                            popUpTo("signup") { inclusive = true }
                        }
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = signUpBtn,
                    contentColor = Color(0xFF1F1F1F)
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .width(140.dp)
                    .height(42.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp),
                        color = Color(0xFF1F1F1F)
                    )
                } else {
                    Text("Sign up")
                }
            }

            Spacer(Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.92f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.White.copy(alpha = 0.55f)
                )
                Text(
                    text = "Or",
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = Color.White.copy(alpha = 0.95f)
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.White.copy(alpha = 0.55f)
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    launcher.launch(googleAuthClient.getSignInIntent())
                },

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF1F1F1F)
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .height(52.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
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
                    Text("Connect with google")
                }
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Already have an account ?",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    navController.navigate("login")
                }
            )

            Spacer(Modifier.height(24.dp))

        }
    }
}