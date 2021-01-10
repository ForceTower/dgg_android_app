package dev.forcetower.destiny.core.service

import dev.forcetower.destiny.core.model.twitch.VideoAuthorization
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface TwitchService {
    @GET("vods/{vodId}/access_token")
    suspend fun vodAuthorization(
        @Path("vodId") vodId: String,
        @Header("Client-ID") clientId: String = "kimne78kx3ncx6brgo4mv6wki5h1ko",
        @Header("Accept") accept: String = "application/vnd.twitchtv.v5+json"
    ): VideoAuthorization

    @GET("channels/{streamer}/access_token")
    suspend fun liveStreamAuthorization(
        @Path("streamer") streamer: String,
        @Header("Client-ID") clientId: String = "kimne78kx3ncx6brgo4mv6wki5h1ko",
        @Header("Accept") accept: String = "application/vnd.twitchtv.v5+json"
    ): VideoAuthorization
}