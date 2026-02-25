package com.example.habitstopper.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.graphics.Brush  //gradient bg color
import androidx.compose.ui.text.input.ImeAction // better keyboard ux
import androidx.compose.ui.text.input.KeyboardType // email/pwd keyboard typpes


@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) } //loading state
    var errorText by remember { mutableStateOf<String?>(null) } //error msg for validation


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
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .widthIn(max=440.dp), //max width for large screens
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "HabitStopper",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Welcome back",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Log in to continue breaking bad habits.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                //error banner
                if (errorText != null){
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = errorText!!,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (errorText != null) errorText=null //clears error when typing
                                    },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    singleLine = true,
                    enabled = !isLoading, // disabled input while loading
                    //better keyboard config
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    //rounded text field
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    //clears error while typing
                    onValueChange = { newValue ->
                        password = newValue
                        if (errorText != null) errorText = null
                                    },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    singleLine = true,
                    enabled = !isLoading, //disable input while loading
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = {showPassword = !showPassword},
                            enabled = !isLoading //disabled during loading
                        ) {
                            Icon(
                                imageVector =
                                    if (showPassword)
                                        Icons.Default.VisibilityOff
                                    else
                                        Icons.Default.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp), //rounded text field shape
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Text(
                        text = "Forgot password?",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable (enabled = !isLoading){
                            //future route
                        }
                    )
                }

                Button(
                    onClick = {
                        if (!validate()) return@Button
                        isLoading = true // show loading state

                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    enabled = !isLoading, //prevent double clicks
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (isLoading){
                        //loading indicator inside button
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text("Logging in ..")
                    }
                    else {
                        Text("Log in")
                    }
                }

                HorizontalDivider() //visual separation

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("No account? ")
                    Text(
                        text = "Sign up",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.clickable { navController.navigate("signup") }
                    )
                }
            }
        }
    }
}