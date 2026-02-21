package io.github.winfeo.superpositiongame.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.lifecycle.viewmodel.compose.viewModel

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import io.github.winfeo.superpositiongame.game.Main
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.github.winfeo.superpositiongame.android.ui.screen.LobbyScreen
import io.github.winfeo.superpositiongame.android.ui.viewModel.LobbyViewModel
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

/** Launches the Android application. */
@SuppressLint("RestrictedApi")
class AndroidLauncher : /*AndroidApplication()*/ ComponentActivity() {
    private val database: FirebaseDatabase = Firebase.database
    private val ref = database.getReference("users")
    private val userId = UUID.randomUUID().toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manageFirebaseUsersListId()
        ///TODO добавить компоуз навигацию
        ///TODO добавить фонт для текста

//        initialize(Main(), AndroidApplicationConfiguration().apply {
//            // Configure your application here.
//            useImmersiveMode = true // Recommended, but not required.
//        })

        setContent {
            val viewModel: LobbyViewModel = viewModel()
            LobbyScreen(viewModel)
        }
    }

    private fun manageFirebaseUsersListId() {
        ref.child(userId).setValue(
            mapOf(
                "createdAt" to getCreatedIdTime(),
                "status" to PlayerStatus.IN_LOBBY.title
            )
        )

        ref.onDisconnect().removeValue()
    }

    private fun getCreatedIdTime(): String {
        val timeMillis = System.currentTimeMillis()
        val date = Date(timeMillis)
        val format = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }
}
