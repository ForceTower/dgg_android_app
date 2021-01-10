package dev.forcetower.destiny.view.stream.chat

import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import dev.forcetower.destiny.DestinyApp
import dev.forcetower.destiny.core.GlideImageGetter

@BindingAdapter("chatMessage1")
fun TextView.chatMessage2(message: String?) {
    val emotes = (context.applicationContext as DestinyApp).emotesSet
    val set = emotes.keys

    val imageGetter = GlideImageGetter(this, false, true, null)

    var text = message ?: ""
    for (emote in set) {
        if (text.contains(emote)) {
            val element = emotes[emote] ?: error("Impossible undefined")
            val html = "<img alt=\"\" src=\"${element.image[0].url}\" width=\"${element.image[0].width}\" height=\"${element.image[0].height}\" />"
            text = text.replace(emote, html)
        }
    }

    val value = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY, imageGetter, null)
    setText(value)
}

@BindingAdapter("chatMessage")
fun TextView.chatMessage(message: String?) {
    val emotes = (context.applicationContext as DestinyApp).emotesList

    val imageGetter = GlideImageGetter(this, false, true, null)

    var text = message ?: ""
    for (emote in emotes) {
        if (text.contains(emote.prefix)) {
            val html = "<img alt=\"\" src=\"${emote.image[0].url}\" width=\"${emote.image[0].width}\" height=\"${emote.image[0].height}\" />"
            text = text.replace(emote.prefix, html)
        }
    }

    val value = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY, imageGetter, null)
    setText(value)
}