package com.example.nuklear

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nuklear.ui.theme.NuKlearTheme

// Password strength helper
fun getPasswordStrength(password: String): Pair<Float, Color> {
    if (password.isEmpty()) return 0f to Color.Gray
    val hasUpper = password.any { it.isUpperCase() }
    val hasDigit = password.any { it.isDigit() }
    val isLongEnough = password.length >= 8

    val score = listOf(hasUpper, hasDigit, isLongEnough).count { it }
    return when (score) {
        1 -> 0.33f to Color.Red
        2 -> 0.66f to Color(0xFFFFC107) // Amber/Yellow
        3 -> 1f to Color(0xFF4CAF50)    // Green
        else -> 0.1f to Color.Red
    }
}
@Composable
fun AuthScreen(
    isLoading: Boolean = false,
    onAuthAction: (String, String, Boolean) -> Unit = { _, _, _ -> }
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isSignUp by remember { mutableStateOf(false) }

    // Simple validation check
    val (strengthProgress, strengthColor) = getPasswordStrength(password)
    val canSubmit = email.isNotEmpty() && password.isNotEmpty() && (!isSignUp || (password == confirmPassword))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isSignUp) "Join NuKlear" else "Welcome Back",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = if (isSignUp) "Create an account to explore stations" else "Login to manage your profile",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        // --- STRENGTH INDICATOR ---
        if (isSignUp && password.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { strengthProgress },
                modifier = Modifier.fillMaxWidth().height(4.dp),
                color = strengthColor,
                trackColor = strengthColor.copy(alpha = 0.2f)
            )
            Text(
                text = when(strengthProgress) {
                    1f -> "Strong Password"
                    0.66f -> "Medium Strength"
                    else -> "Weak (Needs Uppercase & Number)"
                },
                style = MaterialTheme.typography.labelSmall,
                color = strengthColor,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        if (isSignUp) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                isError = confirmPassword.isNotEmpty() && password != confirmPassword
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onAuthAction(email, password, isSignUp) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading && canSubmit
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
            } else {
                Text(if (isSignUp) "Create Account" else "Sign In")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            isSignUp = !isSignUp
            confirmPassword = ""
        }) {
            Text(if (isSignUp) "Already have an account? Log In" else "Don't have an account? Sign Up")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    NuKlearTheme {
        AuthScreen()
    }
}