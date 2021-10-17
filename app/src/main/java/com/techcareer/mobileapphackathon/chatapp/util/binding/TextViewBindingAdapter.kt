package com.techcareer.mobileapphackathon.chatapp.util.binding

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.Timestamp
import com.techcareer.mobileapphackathon.chatapp.R
import com.techcareer.mobileapphackathon.chatapp.repository.home.VoiceMessage
import com.techcareer.mobileapphackathon.chatapp.ui.chat.VoiceMessageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL


/**
 * @author: Hasan Küçük on 11.10.2021
 */

@BindingAdapter("audioTime")
fun bindingAudioTime(textView: AppCompatTextView, audioFile: Long?) {
    audioFile?.let {
        val df = SimpleDateFormat("mm:ss", Locale.getDefault())
        textView.text = df.format(Date(it))
    }
}

@BindingAdapter("hourTime")
fun bindingTextHourTime(textView: TextView, timestamp: Any?) {
    if (timestamp is Timestamp) {
        val df = SimpleDateFormat("HH:mm", Locale.getDefault())
        textView.text = df.format(timestamp.toDate())
    }
}


@BindingAdapter("readIconTint")
fun bindingReadIconTint(view: ImageView, timestamp: Any?) {
    view.setColorFilter(
        if (timestamp != null) ContextCompat.getColor(view.context, R.color.readOn)
        else ContextCompat.getColor(view.context, R.color.readOff)
    )
}

@BindingAdapter("voiceMessageList")
fun bindMsgRecyclerView(recyclerView: RecyclerView, data: List<VoiceMessage>?) {
    val adapter = recyclerView.adapter as VoiceMessageAdapter
    adapter.submitList(data) { recyclerView.layoutManager?.scrollToPosition(adapter.itemCount - 1) }
    adapter.notifyDataSetChanged()
}

@BindingAdapter("fakeAudioProgress")
fun bindingFakeAudioProgress(view: View, voiceMessage: VoiceMessage?) {
    if (voiceMessage == null) return
    CoroutineScope(Dispatchers.Main).launch {
        try {
            view.visibility = View.VISIBLE
            getAudio(voiceMessage)
            voiceMessage.audioDownloaded = true
            view.visibility = View.GONE
        } catch (e: Exception) {
            view.visibility = View.GONE
        }
    }
}

suspend fun getAudio(msg: VoiceMessage) {
    withContext(Dispatchers.IO) { msg.audioFile?.let { msg.audioUrl?.saveTo(it) } }
}


fun String.saveTo(path: String) {
    val file = File(path)
    if (file.exists()) return
    URL(this).openStream().use { input ->
        try {
            if (file.parentFile?.exists() != true) file.parentFile?.mkdirs()
            file.createNewFile()
            FileOutputStream(file).use { output -> input.copyTo(output) }
        } catch (e: Exception) {
            Log.e("TAG", e.toString())
            throw e
        }
    }
}

fun <T, VH : RecyclerView.ViewHolder> ListAdapter<T, VH>.updateList(list: List<T>?) {
    this.submitList(if (list == this.currentList) list.toList() else list)
}