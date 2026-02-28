package io.github.winfeo.superpositiongame.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.github.winfeo.superpositiongame.android.ui.nav.Navigation
import io.github.winfeo.superpositiongame.android.ui.screen.invites.InvitesViewModel
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

/** Launches the Android application. */
@SuppressLint("RestrictedApi") ///TODO добавить DI (или фабрики). Тяжко без него
///TODO перенсти методы в data-слой, в репоизторий какой-нибудь
class AndroidLauncher: ComponentActivity() {
    private val database: FirebaseDatabase = Firebase.database
    private val ref = database.getReference("users")
    private val userId = UUID.randomUUID().toString() ///TODO хранить в SharedPref и не создавать новый каждый раз?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manageFirebaseUsersListId()
        removeThisMethodLater()
        ///TODO добавить фонт для текста

        setContent {
            Navigation(currentUserId = userId)///TODO без параметров, переделать
        }
    }

    private fun manageFirebaseUsersListId() {
        val userRef = ref.child(userId)
        val connectedRef = database.getReference(".info/connected")

        connectedRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isConnected = snapshot.getValue(Boolean::class.java) ?: false

                if (isConnected) {
                    userRef.setValue(
                        mapOf(
                            "createdAt" to getCreatedIdTime(),
                            "status" to PlayerStatus.IN_LOBBY.title
                        )

                    )

                    ///TODO давать некоторое время на удержание сокета, не удалять сразу при навигации из приложения
                    ref.child(userId).onDisconnect().removeValue()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


    }

    ///TODO использовать метод addListenerToInvitation из ViewModel, когда DI добавлю
    private fun removeThisMethodLater() {
        val invitesRef = database.getReference("invitations")

        invitesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                snapshot.children.forEach { inviteSnapshot ->
                    val fromUserId = inviteSnapshot.child("fromUserId")
                        .getValue(String::class.java)

                    val toUserId = inviteSnapshot.child("toUserId")
                        .getValue(String::class.java)

                    if (userId == fromUserId || userId == toUserId) {
                        inviteSnapshot.ref.onDisconnect().removeValue()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    ///TODO добавить как утилиту?
    private fun getCreatedIdTime(): String {
        val timeMillis = System.currentTimeMillis()
        val date = Date(timeMillis)
        val format = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }
}
