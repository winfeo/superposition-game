package io.github.winfeo.superpositiongame.android.ui.screen.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import io.github.winfeo.superpositiongame.Main

class GameFragment: AndroidFragmentApplication() {
    lateinit var game: Main

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return initializeForView(
            game,
            AndroidApplicationConfiguration().apply {
                useImmersiveMode = true
            }
        )
    }

    override fun exit() {
        requireActivity().finish()
    }
}
