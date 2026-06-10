package io.github.winfeo.superpositiongame.android

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import io.github.winfeo.superpositiongame.android.data.source.socket.Network
import io.github.winfeo.superpositiongame.android.data.source.AppModule
import io.github.winfeo.superpositiongame.android.data.source.local.UserSession
import io.github.winfeo.superpositiongame.android.data.util.toDomain
import io.github.winfeo.superpositiongame.android.ui.nav.Navigation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi")
class AndroidLauncher : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppModule.init(applicationContext)

        val savedToken = AppModule.tokenManager.getToken()
        if (savedToken != null) {
            UserSession.restoreToken(savedToken)
            lifecycleScope.launch {
                val result = AppModule.userRepository.getCurrentUser()
                result.fold(
                    onSuccess = { userDto ->
                        val user = userDto.toDomain()
                        UserSession.restoreSession(user, savedToken)
                        UserSession.setUserId(user.id.toString())
                        Network.connect(userId = user.id.toString())
                    },
                    onFailure = {
                        UserSession.logout()
                        startGuestMode()
                    }
                )
            }
        } else {
            startGuestMode()
        }

        setContent {
            val userIdState by UserSession.currentUserId.collectAsState()
            val currentUserId = userIdState
            if (currentUserId == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        color = Color(0xFF6C8CFF)
                    )
                }
            } else {
                Navigation()
            }
        }
    }

    private fun startGuestMode() {
        lifecycleScope.launch {
            while (true) {
                val guestIdResult = AppModule.guestRepository.createGuest()
                if (guestIdResult.isSuccess) {
                    val guestId = guestIdResult.getOrNull()!!
                    UserSession.setUserId(guestId)
                    Network.connect(userId = guestId)
                    Log.i("AndroidLauncher", "Гостевой ID получен: $guestId")
                    break
                } else {
                    Log.e("AndroidLauncher", "Не удалось получить гостевой ID")
                    delay(3000)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Network.disconnect()
    }
}
