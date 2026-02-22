package com.example.habitstopper.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun SignUpScreen(navController: NavController) {
    Column() {
        Button (onClick = { navController.popBackStack() }) { Text("Back to Login") }
        Button(onClick = { navController.navigate("home") }) { Text("Create Account → Home") }
    }

}