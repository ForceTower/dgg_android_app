package dev.forcetower.destiny.core.task

import dev.forcetower.destiny.core.service.TwitchService
import timber.log.Timber

class GetVodStreamTask(
    private val service: TwitchService,
    private val vodId: String
) : BaseTask<String> {
    override suspend fun execute(): String {
        val authorization = service.vodAuthorization(vodId)
        val token = authorization.token.replace("\\", "")
        Timber.d("Token $token")
        return "http://usher.twitch.tv/vod/$vodId?nauthsig=${authorization.signature}&nauth=${token}"
    }
}