package io.github.winfeo.superpositiongame.android.data.source

object NetworkConfig {
//    private const val SERVER_ADDRESS = "10.0.2.2:8080"
    private const val SERVER_ADDRESS = "superposition.pavlenty.uk"

    const val REST_BASE_URL = "https://$SERVER_ADDRESS"
    const val STOMP_URL = "wss://$SERVER_ADDRESS/ws-android"
}
