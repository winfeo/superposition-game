package io.github.winfeo.superpositiongame.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Label
import io.github.winfeo.superpositiongame.config.GameConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameTimer(
    private val label: Label,
    private val scope: CoroutineScope
) {
    private val duration: Int = GameConfig.getTimerDuration()
//    private val progressBar = ProgressBar(0f, 30f,1f,false,skin)
    private var job: Job? = null

    fun start(timeOut: () -> Unit) {
        finish()
        job = scope.launch {
            var leftTime = duration
            while (leftTime > 0) {
                Gdx.app.postRunnable {
                    label.setText(leftTime)
                }
                delay(1000)
                leftTime--
            }
            timeOut()
        }
    }

    fun finish() {
        job?.cancel()
        job = null
    }
}
