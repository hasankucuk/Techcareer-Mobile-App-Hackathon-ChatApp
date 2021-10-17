package com.techcareer.mobileapphackathon.chatapp.util

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.arthenica.mobileffmpeg.FFmpeg
import com.techcareer.mobileapphackathon.chatapp.R
import com.techcareer.mobileapphackathon.chatapp.repository.home.VoiceMessage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AudioHelper(
    private val activity: AppCompatActivity
) {
    val RECORD_PREFIX = "AUDIO"
    val RECORDS_FOLDER_NAME = "Records"
    val RECORDS_FOLDER_NAME_NEW = "NewRecords"
    val DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

    //    val RECORD_EXT = ".3gp"
    val RECORD_EXT = ".mp3"

    private var playButton: View? = null
    private var seekBar: SeekBar? = null
    private var audioTimeTextView: AppCompatTextView? = null

    private var playFileName: String? = null
    var recordFileName: String? = null
    var recordFileNameNew: String? = null
    private var player: MediaPlayer? = null
    private var recorder: MediaRecorder? = null

    private var timer: Timer? = null
    private var pauseTime: Int? = null
    var recorderDuration: Long? = null
    lateinit var recorderStatus: AudioRecorderStatus

    fun setAudioRecorderStatus(audioRecorderStatus: AudioRecorderStatus) {
        recorderStatus = audioRecorderStatus
    }

    fun setupAudioHelper(view: View, message: VoiceMessage) {
        if (playButton != view) {
            pauseTime = player?.currentPosition
            stopPlaying()
            playButton?.isActivated = false
        }

        if (view.isActivated.not()) {
            playButton = view
            val parentView = view.parent as ConstraintLayout
            seekBar = parentView.findViewById(R.id.seekBar)
            audioTimeTextView = parentView.findViewById(R.id.audioTimeTextView)
            playFileName = message.audioFile
            stopPlaying()
            startPlaying()
            view.isActivated = true
        } else {
            pauseTime = player?.currentPosition
            stopPlaying()
            view.isActivated = false
        }
    }

    private fun setSeekBarTimer() {
        timer = Timer().apply { scheduleAtFixedRate(getTimerTask(), 0, 20) }
    }

    private fun getTimerTask() = object : TimerTask() {
        override fun run() {
            if (player == null) timer?.cancel() else activity.runOnUiThread { setSeekBarPosition() }
        }
    }

    private fun setSeekBarPosition() {
        val player = player ?: return
        val audioTimeTextView = audioTimeTextView ?: return
        seekBar?.progress = player.currentPosition * 100 / player.duration
        setAudioTimeMMSS(audioTimeTextView, player.currentPosition.toLong())
    }

    fun setAudioTime(seekBar: SeekBar, textView: AppCompatTextView, duration: Long) {
        val time = duration * seekBar.progress / 100
        setAudioTimeMMSS(textView, time)
    }

    fun setAudioTimeMMSS(textView: AppCompatTextView, duration: Long) {
        val df = SimpleDateFormat("mm:ss", Locale.getDefault())
        textView.text = df.format(Date(duration))
    }

    fun startPlaying() {
        player = MediaPlayer().apply {
            setOnCompletionListener { onCompleted() }
            try {
                setDataSource(playFileName); prepare(); start()
                seekTo(getTime(this)); setSeekBarTimer()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private fun recordRobotVoice() {

        val cmd = arrayOf(
            "-y",
            "-i",
            recordFileName!!,
            "-af",
            "asetrate=11100,atempo=4/3,atempo=1/2,atempo=3/4",
            recordFileNameNew!!
        ) //Robot

        exceuteFFMPEG(cmd)

    }

    private fun recordCaveVoice() {
        val cmd = arrayOf(
            "-y",
            "-i",
            recordFileName!!,
            "-af",
            "aecho=0.8:0.9:1000:0.3",
            recordFileNameNew!!
        )
        exceuteFFMPEG(cmd)
    }

    private fun recordReverseVoice() {
        val cmd = arrayOf(
            "-y",
            "-i",
            recordFileName!!,
            "-filter_complex",
            "areverse",
            recordFileNameNew!!
        )
        exceuteFFMPEG(cmd)
    }

    private fun recordVibrationVoice() {
        val cmd = arrayOf(
            "-y",
            "-i",
            recordFileName!!,
            "-filter_complex",
            "vibrato=f=10",
            recordFileNameNew!!
        )
        exceuteFFMPEG(cmd)
    }

    private fun exceuteFFMPEG(cmd: Array<String>?) {

        FFmpeg.execute(cmd)
        val rc = FFmpeg.getLastReturnCode()
        val output = FFmpeg.getLastCommandOutput()
        if (rc == FFmpeg.RETURN_CODE_SUCCESS) {
            Log.i("GetInfo", "Command execution completed successfully.")
            recorderStatus.onCompleteRecord()
        } else if (rc == FFmpeg.RETURN_CODE_CANCEL) {
            Log.i("GetInfo", "Command execution cancelled by user.")
        } else {
            Log.i(
                "GetInfo",
                String.format(
                    "Command execution failed with rc=%d and output=%s.",
                    rc,
                    output
                )
            )
        }
    }

    private fun MediaPlayer.onCompleted() {
        audioTimeTextView?.let { setAudioTimeMMSS(it, duration.toLong()) }
        stopPlaying()
        playButton?.isActivated = false
        seekBar?.progress = 0
    }

    private fun getTime(mediaPlayer: MediaPlayer): Int {
        val seekBarProgress = seekBar?.progress ?: return 0
        return mediaPlayer.duration * seekBarProgress / 100
    }

    fun stopPlaying() {
        stopTimer()
        try {
            player?.apply { stop(); release() }
            player = null
        } catch (stopException: RuntimeException) {
            stopException.printStackTrace()
        }
    }

    fun stopTimer() {
        timer?.cancel(); timer = null
    }

    fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(getRecordFilePath())
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            try {
                prepare(); start()
                recorderDuration = System.currentTimeMillis()
            } catch (e: IOException) {
                Log.e("AUDIO_LOG", "prepare() failed")
            } catch (e: IllegalStateException) {
                Log.e("AUDIO_LOG", "prepare() failed")
            }
        }
    }

    private fun getRecordFilePath(): String? {
        return createMediaFile(
            RECORDS_FOLDER_NAME,
            DATE_FORMAT,
            RECORD_PREFIX,
            RECORD_EXT
        )?.absolutePath.apply { recordFileName = this }
    }

    private fun getRecordFilePathNew(): String? {
        return createMediaFile(
            RECORDS_FOLDER_NAME_NEW,
            DATE_FORMAT,
            RECORD_PREFIX,
            RECORD_EXT
        )?.absolutePath.apply { recordFileNameNew = this }
    }

    private fun createMediaFile(
        folderName: String,
        dateFormat: String,
        prefix: String,
        extension: String
    ): File? {
        var voiceFile: File? = null
        try {
            val timeStamp: String = SimpleDateFormat(dateFormat, Locale.ROOT).format(Date())
            val storageDir = getOutputDirectory(activity, folderName)
            voiceFile = File(storageDir, "${prefix}_${timeStamp}$extension")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return voiceFile
    }

    private fun getOutputDirectory(context: Context, folderName: String): File {
        val appContext = context.applicationContext
        val rootPath = appContext.resources.getString(R.string.app_name) + "/" + folderName
        val mediaDir = appContext.externalMediaDirs.firstOrNull()?.let {
            File(it, rootPath).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else appContext.filesDir
    }

    fun stopRecording(voiceEffectType: VoiceEffectType? = null) {

        recorderDuration?.let { recorderDuration = System.currentTimeMillis().minus(it) }
        try {
            recorder?.apply { stop(); release() }
            recorder = null
        } catch (stopException: RuntimeException) {
            Log.e("AUDIO_LOG", "stop() failed")
        }
        getRecordFilePathNew()
        when (voiceEffectType) {
            VoiceEffectType.NORMAL -> {
                recordFileNameNew = recordFileName
                recorderStatus.onCompleteRecord()
            }
            VoiceEffectType.CAVE -> {
                recordCaveVoice()
            }
            VoiceEffectType.REVERSE -> {
                recordReverseVoice()
            }
            VoiceEffectType.ROBOTIC -> {
                recordRobotVoice()
            }
            VoiceEffectType.VIBRATION -> {
                recordVibrationVoice()
            }
            null -> {
            }
        }


    }

    fun onStop() {
        stopPlaying()
        stopRecording()
    }


}

abstract class AudioRecorderStatus {
    abstract fun onCompleteRecord()
}