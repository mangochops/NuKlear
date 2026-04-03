package com.example.nuklear

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    val email: String,
    val full_name: String? = null,
    val avatar_url: String? = null
)
