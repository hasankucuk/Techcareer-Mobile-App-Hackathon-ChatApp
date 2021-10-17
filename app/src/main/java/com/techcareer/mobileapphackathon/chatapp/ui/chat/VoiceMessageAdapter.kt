package com.techcareer.mobileapphackathon.chatapp.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.techcareer.mobileapphackathon.chatapp.R
import com.techcareer.mobileapphackathon.chatapp.databinding.ItemLeftVoiceMessageBinding
import com.techcareer.mobileapphackathon.chatapp.databinding.ItemRightVoiceMessageBinding
import com.techcareer.mobileapphackathon.chatapp.repository.home.VoiceMessage
import com.techcareer.mobileapphackathon.chatapp.BR

/**
 * @author: Hasan Küçük on 11.10.2021
 */
class VoiceMessageAdapter(
    private val voiceMessageListener: VoiceMessageListener
) : ListAdapter<VoiceMessage, RecyclerView.ViewHolder>(VoiceMessageDiffCallback) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = getItem(position)
        (holder as VoiceMessageViewHolder<*>).bind(voiceMessageListener, msg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_right_voice_message -> ItemRightVoiceMessageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            R.layout.item_left_voice_message -> ItemLeftVoiceMessageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }.let { VoiceMessageViewHolder(it) }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)!!
        return if (item.isOwner!!) {
            R.layout.item_right_voice_message
        } else {
            R.layout.item_left_voice_message
        }
    }

    class VoiceMessageViewHolder<T : ViewDataBinding> constructor(val binding: T) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(voiceMessageListener: VoiceMessageListener, item: VoiceMessage) {
            binding.setVariable(BR.item, item)
            binding.setVariable(BR.clickListener, voiceMessageListener)
            binding.executePendingBindings()
            val seekBar = binding.root.findViewById<SeekBar>(R.id.seekBar)
            seekBar?.setOnSeekBarChangeListener(
                getSeekBarChangeListener(
                    voiceMessageListener,
                    item
                )
            )
        }

        private fun getSeekBarChangeListener(
            voiceMessageListener: VoiceMessageListener,
            item: VoiceMessage
        ): SeekBar.OnSeekBarChangeListener {
            return object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    voiceMessageListener.onStartTrackingTouch(seekBar, item)
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    voiceMessageListener.onStopTrackingTouch(seekBar, item)
                }
            }
        }
    }

    companion object VoiceMessageDiffCallback : DiffUtil.ItemCallback<VoiceMessage>() {
        override fun areItemsTheSame(oldItem: VoiceMessage, newItem: VoiceMessage): Boolean {
            return oldItem.audioUrl.contentEquals(newItem.audioUrl)
        }

        override fun areContentsTheSame(oldItem: VoiceMessage, newItem: VoiceMessage): Boolean {
            return oldItem == newItem
        }
    }

    interface VoiceMessageListener {
        fun onAudioClick(view: View, message: VoiceMessage)
        fun onStartTrackingTouch(seekBar: SeekBar, item: VoiceMessage)
        fun onStopTrackingTouch(seekBar: SeekBar, item: VoiceMessage)
    }
}