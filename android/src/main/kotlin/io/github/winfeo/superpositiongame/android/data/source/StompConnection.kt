package io.github.winfeo.superpositiongame.android.data.source

import android.util.Log
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader

object StompConnection {
    private const val HOST = "ws://10.0.2.2:8080/ws/websocket"
    lateinit var client: StompClient
        private set

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private var lifecycleDisposable: Disposable? = null

    fun connect(
        userId: String
    ) {
        client = Stomp.over(Stomp.ConnectionProvider.OKHTTP, HOST)

        lifecycleDisposable = client.lifecycle()
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
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

        val headers = listOf(StompHeader("userId", userId))
        client.connect(headers)
    }

    fun disconnect() {
        _isConnected.value = false
        lifecycleDisposable?.dispose()
        client.disconnect()
    }
}
