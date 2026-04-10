package io.github.winfeo.superpositiongame.android

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.github.winfeo.superpositiongame.android.data.source.Network
import io.github.winfeo.superpositiongame.android.ui.nav.Navigation
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

/** Launches the Android application. */
@SuppressLint("RestrictedApi") ///TODO добавить DI (или фабрики). Тяжко без него
///TODO перенсти методы в data-слой, в репоизторий какой-нибудь
class AndroidLauncher: ComponentActivity() {

    ///TODO хранить в SharedPref и не создавать новый каждый раз?
    ///Генерировать id на сервере?
    private val userId = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ///TODO добавить фонт для текста

        setContent {
            Navigation(currentUserId = userId)///TODO без параметров, переделать
        }

        Network.connect(userId = userId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Network.disconnect()
    }
}
