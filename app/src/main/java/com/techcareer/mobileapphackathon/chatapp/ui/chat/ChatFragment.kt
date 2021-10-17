package com.techcareer.mobileapphackathon.chatapp.ui.chat

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.techcareer.mobileapphackathon.chatapp.R
import com.techcareer.mobileapphackathon.chatapp.databinding.FragmentChatBinding
import com.techcareer.mobileapphackathon.chatapp.repository.home.VoiceMessage
import com.techcareer.mobileapphackathon.chatapp.util.AudioHelper
import com.techcareer.mobileapphackathon.chatapp.util.AudioRecorderStatus
import com.techcareer.mobileapphackathon.chatapp.util.VoiceEffectType
import com.techcareer.mobileapphackathon.chatapp.util.binding.bindingFakeAudioProgress
import com.techcareer.mobileapphackathon.chatapp.util.binding.updateList
import com.techcareer.mobileapphackathon.common.base.BaseFragment
import com.techcareer.mobileapphackathon.common.util.exteinsion.gone
import com.techcareer.mobileapphackathon.common.util.exteinsion.isPermissionGranted
import com.techcareer.mobileapphackathon.common.util.exteinsion.permissionResult
import com.techcareer.mobileapphackathon.common.util.exteinsion.visible
import com.tougee.recorderview.AudioRecordView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * @author: Hasan Küçük on 11.10.2021
 */

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>(), AudioRecordView.Callback {

    override fun getLayoutId(): Int = R.layout.fragment_chat

    private val chatViewModel: ChatViewModel by activityViewModels()

    lateinit var audioHelper: AudioHelper

    lateinit var voiceMessageAdapter: VoiceMessageAdapter

    private var voiceEffectType: VoiceEffectType = VoiceEffectType.NORMAL

    private val lastSendVoiceHolder = VoiceMessage(
        audioUrl = "_",
        audioFile = "_",
        isOwner = true
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = chatViewModel
        audioHelper = AudioHelper(requireActivity() as AppCompatActivity)

        if (isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
            chatViewModel.onScreenState(ChatStateModel.PermissionGranted)
        } else {
            chatViewModel.onScreenState(ChatStateModel.PermissionDenied)
        }

        lifecycleScope.launch {
            chatViewModel.chatModel.collect {
                requireActivity().title = it?.receiver?.displayName ?: "Voice Chat"
            }
        }

        lifecycleScope.launch {
            chatViewModel.getChatRoomMessageList().collect {
                if (it.isNullOrEmpty() && isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
                    chatViewModel.onScreenState(ChatStateModel.ChatListEmpty)
                }
                voiceMessageAdapter.updateList(it).run {
                    binding.voiceMessageRecyclerView.layoutManager?.scrollToPosition(
                        voiceMessageAdapter.itemCount - 1
                    )
                }
                voiceMessageAdapter.notifyDataSetChanged()
            }
        }

        binding.effectTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
            voiceEffectType = when (checkedId) {
                R.id.chipVoiceTypeNormal -> {
                    VoiceEffectType.NORMAL
                }
                R.id.chipVoiceTypeVibration -> {
                    VoiceEffectType.VIBRATION
                }
                R.id.chipVoiceTypeCave -> {
                    VoiceEffectType.CAVE
                }
                R.id.chipVoiceTypeReverse -> {
                    VoiceEffectType.REVERSE
                }
                R.id.chipVoiceTypeRobot -> {
                    VoiceEffectType.ROBOTIC
                }
                else -> {
                    VoiceEffectType.NORMAL
                }
            }
        }
        binding.btnPermission.setOnClickListener { microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO) }

        voiceMessageAdapter =
            VoiceMessageAdapter(object : VoiceMessageAdapter.VoiceMessageListener {
                override fun onAudioClick(view: View, message: VoiceMessage) {
                    if (message.audioDownloaded.not()) bindingFakeAudioProgress(view, message)
                    else audioHelper.setupAudioHelper(view, message)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar, item: VoiceMessage) {
                    val parentView = seekBar.parent as ConstraintLayout
                    val playButton = parentView.findViewById<View>(R.id.playButton)
                    if (playButton.isActivated) audioHelper.stopTimer()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar, item: VoiceMessage) {
                    val parentView = seekBar.parent as ConstraintLayout
                    val playButton = parentView.findViewById<View>(R.id.playButton) ?: return
                    if (playButton.isActivated) {
                        audioHelper.stopTimer()
                        audioHelper.stopPlaying()
                        audioHelper.startPlaying()
                    } else {
                        val audioTimeView =
                            parentView.findViewById<AppCompatTextView>(R.id.audioTimeTextView)
                        val audioDuration = item.audioDuration ?: return
                        audioHelper.setAudioTime(seekBar, audioTimeView, audioDuration)
                    }
                }
            })
        binding.voiceMessageRecyclerView.adapter = voiceMessageAdapter

        binding.recordView.apply {
            activity = requireActivity()
            callback = this@ChatFragment
        }
        binding.sendButton.setOnTouchListener { view, motionEvent ->
//            when (motionEvent.action) {
//                MotionEvent.ACTION_UP -> view.isPressed = false
//                MotionEvent.ACTION_DOWN -> view.isPressed = true
//            }

            onMicPress(motionEvent)
        }
    }

    fun onMicPress(motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> audioHelper.startRecording()
            MotionEvent.ACTION_UP -> {
                audioHelper.stopRecording()
                val recordFileName = audioHelper.recordFileName ?: return false
                val recorderDuration = audioHelper.recorderDuration ?: 0
                if (recorderDuration > 1000) sendVoiceMessage(recordFileName, recorderDuration)
            }
        }
        return false
    }

    private fun sendVoiceMessage(recordFileName: String, recorderDuration: Long) {
        chatViewModel.onScreenState(ChatStateModel.PermissionGranted)
        lastSendVoiceHolder.apply {
            audioFile = recordFileName
            audioDuration = recorderDuration
        }
        chatViewModel.sendVoiceMessage(recordFileName, recorderDuration)
        val newList = mutableListOf<VoiceMessage>()
        newList.addAll(voiceMessageAdapter.currentList)
        newList.add(lastSendVoiceHolder)
        voiceMessageAdapter.submitList(newList) { scrollToPosition() }
        voiceMessageAdapter.notifyDataSetChanged()
    }

    private fun scrollToPosition() {
        binding.voiceMessageRecyclerView.layoutManager?.scrollToPosition(voiceMessageAdapter.itemCount - 1)
    }


    private val microphonePermissionLauncher = permissionResult {
        onGranted = {
            chatViewModel.onScreenState(ChatStateModel.PermissionGranted)
        }
        onDenied = {}
    }

    override fun isReady(): Boolean {
        return true
    }

    override fun onRecordCancel() {
        binding.tvInfoText.visible()
        audioHelper.stopRecording()
    }

    override fun onRecordEnd() {
        binding.tvInfoText.visible()
        audioHelper.setAudioRecorderStatus(object : AudioRecorderStatus() {
            override fun onCompleteRecord() {
                val recordFileName = audioHelper.recordFileNameNew
                val recorderDuration = audioHelper.recorderDuration ?: 0
                recordFileName?.let {
                    if (recorderDuration > 1000) sendVoiceMessage(recordFileName, recorderDuration)
                }
            }
        })

        audioHelper.stopRecording(voiceEffectType)
    }

    override fun onRecordStart() {
        binding.tvInfoText.gone()
        audioHelper.startRecording()
    }

    override fun onStop() {
        super.onStop()
        audioHelper.onStop()
    }

}