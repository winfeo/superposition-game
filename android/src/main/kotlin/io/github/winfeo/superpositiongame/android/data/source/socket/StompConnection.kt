package io.github.winfeo.superpositiongame.android.data.source.socket

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent

object StompConnection {
//    private const val HOST = "ws://10.0.2.2:8080/ws/websocket"
    private const val HOST = "ws://10.0.2.2:8080/ws-android"
//    private const val HOST = "ws://91.237.249.20:8080/ws-android"
    lateinit var client: StompClient
        private set

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private var lifecycleDisposable: Disposable? = null

    fun connect(
        userId: String
    ) {
        val url = "$HOST?userId=$userId" ///TODO переделать
        Log.d("STOMP", "Подключение к: $url")
        client = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)
//        client = Stomp.over(Stomp.ConnectionProvider.OKHTTP, HOST)

        lifecycleDisposable = client.lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { event ->
                when (event.type) {
                    LifecycleEvent.Type.OPENED -> {
                        _isConnected.value = true
                        Log.d("STOMP", "STOMP соединение открыто")
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        _isConnected.value = false
                        Log.d("STOMP", "STOMP соединение закрыто")
                    }
                    LifecycleEvent.Type.ERROR -> {
                        Log.d("STOMP", "STOMP ошибка соединения: ${event.exception}")
                    }
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {}
                }
            }

//        val headers = listOf(StompHeader("userId", userId)) ///TODO не работают кастомные заголовки?
//        client.connect(headers)
        client.connect()
    }

    fun disconnect() {
        _isConnected.value = false
        lifecycleDisposable?.dispose()
        client.disconnect()
    }
}
