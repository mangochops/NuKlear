package com.example.nuklear

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    var isLoading = mutableStateOf(false)
    var authError = mutableStateOf<String?>(null)
    var successMessage = mutableStateOf<String?>(null)

    fun handleAuth(email: String, password: String, isSignUp: Boolean, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            authError.value = null
            successMessage.value = null
            try {
                if (isSignUp) {
                    Supabase.client.auth.signUpWith(Email) {
                        this.email = email
                        this.password = password
                    }
                    successMessage.value = "Account created! Check your email for verification."
                } else {
                    Supabase.client.auth.signInWith(Email) {
                        this.email = email
                        this.password = password
                    }
                    successMessage.value = "Welcome back to NuKlear!"
                }
                onSuccess()
            } catch (e: Exception) {
                authError.value = e.localizedMessage ?: "An error occurred"
            } finally {
                isLoading.value = false
            }
        }
    }
    fun logout() {
        viewModelScope.launch {
            try {
                Supabase.client.auth.signOut()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

