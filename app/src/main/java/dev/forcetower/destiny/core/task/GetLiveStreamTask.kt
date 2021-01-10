package dev.forcetower.destiny.core.task

import dev.forcetower.destiny.core.service.TwitchService
import okhttp3.HttpUrl.Companion.toHttpUrl

class GetLiveStreamTask(
    private val service: TwitchService,
    private val streamer: String
) : BaseTask<String> {
    override suspend fun execute(): String {
        val authorization = service.liveStreamAuthorization(streamer)
        val token = authorization.token.replace("\\", "")

        return "http://usher.twitch.tv/api/channel/hls/$streamer.m3u8"
            .toHttpUrl()
            .newBuilder()
            .addQueryParameter("player", "twitchweb")
            .addQueryParameter("token", token)
            .addQueryParameter("sig", authorization.signature)
            .addQueryParameter("allow_audio_only", "true")
            .addQueryParameter("allow_source", "true")
            .addQueryParameter("type", "any")
            .addQueryParameter("p", (0..6).random().toString())
            .build()
            .toString()
    }
}