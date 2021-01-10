package dev.forcetower.destiny.core.model.twitch

import com.google.gson.annotations.SerializedName

data class VideoAuthorization(
    val token: String,
    @SerializedName("sig")
    val signature: String,
    @SerializedName("expires_at")
    val expiresAt: String
)