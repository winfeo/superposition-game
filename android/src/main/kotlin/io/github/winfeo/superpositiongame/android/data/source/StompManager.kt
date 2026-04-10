package io.github.winfeo.superpositiongame.android.data.source

import android.util.Log
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object StompManager {
    private val pendingSubscriptions = mutableListOf<() -> Unit>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            StompConnection.isConnected.collect { connected ->
                if (connected) {
                    pendingSubscriptions.forEach { it() }
                    pendingSubscriptions.clear()
                }
            }
        }
    }
    fun connect(userId: String) {
        StompConnection.connect(userId)
    }

    fun subscribe(
        topic: String,
        onMessage: (String) -> Unit
    ) {
        val action = {
            StompSubscription.subscribe(
                topic = topic,
                client = StompConnection.client,
                onMessage = onMessage
            )
        }

        if (StompConnection.isConnected.value) {
            action()
        } else {
            pendingSubscriptions.add(action)
        }
    }

    fun unsubscribe(topic: String) {
        StompSubscription.unsubscribe(topic)
    }

    fun send(
        destination: String,
        message: String
    ) {
        if (!StompConnection.isConnected.value) return

        StompConnection.client.send(destination, message)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    Log.d("STOMP", "Сообщение отправлено")
                },
                { error ->
                    Log.d("STOMP", "Ошибка отправки на $destination: ${error.message}")
                }
            )
    }

    fun disconnect() {
        pendingSubscriptions.clear()
        StompConnection.disconnect()
        StompSubscription.clear()
    }
}
