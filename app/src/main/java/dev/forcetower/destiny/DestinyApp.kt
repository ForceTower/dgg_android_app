package dev.forcetower.destiny

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.HiltAndroidApp
import dev.forcetower.destiny.core.model.chat.ChatEmote
import okhttp3.internal.toImmutableMap
import timber.log.Timber
import java.nio.charset.Charset

@HiltAndroidApp
class DestinyApp : Application() {
    lateinit var emotesSet: Map<String, ChatEmote>
    lateinit var emotesList: List<ChatEmote>

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        prepareEmotes()
    }

    private fun prepareEmotes() {
        val stream = resources.openRawResource(R.raw.emotes)

        val size = stream.available()
        val buffer = ByteArray(size)
        stream.read(buffer)
        stream.close()
        val json = String(buffer, Charset.forName("UTF-8"))

        val type = object : TypeToken<List<ChatEmote>>() {}.type
        val emotes = Gson().fromJson<List<ChatEmote>>(json, type)
        Timber.d("Emotes: $emotes")

        val map = mutableMapOf<String, ChatEmote>()
        emotes.forEach { map[it.prefix] = it }
        emotesSet = map.toImmutableMap()
        emotesList = emotes.sortedByDescending { it.prefix.length }
    }
}