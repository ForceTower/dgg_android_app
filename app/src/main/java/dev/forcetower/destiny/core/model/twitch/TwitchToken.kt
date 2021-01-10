package dev.forcetower.destiny.core.model.twitch

import com.google.gson.annotations.SerializedName

data class TwitchToken(
    val authorization: TwitchAuth,
    val chansub: ChanSub,
    @SerializedName("device_id")
    val deviceId: String,
    val expires: Long,
    @SerializedName("https_required")
    val httpsRequired: Boolean,
    val privileged: Boolean,
    @SerializedName("user_id")
    val userId: Long?,
    val version: Int,
    @SerializedName("vod_id")
    val vodId: Long
)

data class TwitchAuth(
    val forbidden: Boolean,
    val reason: String
)

data class ChanSub(
    @SerializedName("restricted_bitrates")
    val restrictedBitrates: List<Any>
)