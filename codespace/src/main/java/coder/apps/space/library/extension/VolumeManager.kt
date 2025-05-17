package coder.apps.space.library.extension

import android.content.Context
import android.media.AudioManager

fun Context.setVolume(streamType: Int, volume: Int) {
    audioManager.setStreamVolume(streamType, volume, 0)
}

fun Context.getVolume(streamType: Int): Int {
    return audioManager.getStreamVolume(streamType)
}

fun Context.increaseVolume(stream: Int = AudioManager.STREAM_MUSIC) {
    val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI)
}

fun Context.decreaseVolume(stream: Int = AudioManager.STREAM_MUSIC) {
    val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI)
}

fun Context.muteVolume(stream: Int = AudioManager.STREAM_MUSIC) {
    val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.adjustStreamVolume(stream, 0, AudioManager.FLAG_SHOW_UI)
}

fun Context.getCurrentVolume(stream: Int = AudioManager.STREAM_MUSIC): Int {
    val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    return audioManager.getStreamVolume(stream)
}

fun Context.getMaxVolume(stream: Int = AudioManager.STREAM_MUSIC): Int {
    val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    return audioManager.getStreamMaxVolume(stream)
}
