package io.github.winfeo.superpositiongame.android.ui.screen.settings

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsManager(context: Context) {
    companion object {
        private const val KEY_MUSIC = "music_enabled"
        private const val KEY_INVITE_SOUND = "invite_sound_enabled"
    }

    private val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    private val _isMusicEnabled = MutableStateFlow(prefs.getBoolean(KEY_MUSIC, true))
    val isMusicEnabled: StateFlow<Boolean> = _isMusicEnabled

    private val _isInviteSoundEnabled = MutableStateFlow(prefs.getBoolean(KEY_INVITE_SOUND, true))
    val isInviteSoundEnabled: StateFlow<Boolean> = _isInviteSoundEnabled

    fun setMusicEnabled(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_MUSIC, enabled) }
        _isMusicEnabled.value = enabled
        //TODO запустить/остановить музыку
    }

    fun setInviteSoundEnabled(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_INVITE_SOUND, enabled) }
        _isInviteSoundEnabled.value = enabled
        //TODO звуки приглашений
    }
}
