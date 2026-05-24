@file:JvmName("Lwjgl3Launcher")

package io.github.winfeo.superpositiongame.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import io.github.winfeo.superpositiongame.Main
import io.github.winfeo.superpositiongame.graphics.Dialogs
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.dice.DiceState
import io.github.winfeo.superpositiongame.model.game.GamePhase
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.PlayerState
import io.github.winfeo.superpositiongame.model.game.SlotOwner

/** Launches the desktop (LWJGL3) application. */
fun main() {
    // This handles macOS support and helps on Windows.
    if (StartupHelper.startNewJvmIfRequired())
      return

    val mockDialogs = object: Dialogs {
        override fun showRotateCardDialog(
            availableStates: List<DiceState>,
            onStateSelected: (DiceState) -> Unit
        ) {}
        override fun showReshuffleDialog(
            cards: List<Card>,
            maxSelectable: Int,
            minSelectable: Int,
            onCardsSelected: (List<Card>) -> Unit
        ) {}
        override fun showCardPreview(card: Card) {}
    }

    val mockGetGameState: () -> GameState = {
        GameState(
            phase = GamePhase.DEAL_CARDS,
            turnNumber = 1,
            currentPlayerId = "1234",
            players = mapOf(
                "1234" to PlayerState(
                    id = "1234",
                    hand = emptyList(),
                    slots = emptyList(),
                    skipNextTurn = false,
                    remainingMoves = 1
                ),
                "5678" to PlayerState(
                    id = "5678",
                    hand = emptyList(),
                    slots = emptyList(),
                    skipNextTurn = false,
                    remainingMoves = 1
                )
            ),
            activeSlotsRow = SlotOwner.OPPONENT,
            winnerId = null
        )
    }

    Lwjgl3Application(Main(
        playerId = "1234",
        dialogs = mockDialogs,
        onMove = {},
        getGameState = mockGetGameState
    ), Lwjgl3ApplicationConfiguration().apply {
        setTitle("Superposition Game")
        //// Vsync limits the frames per second to what your hardware can display, and helps eliminate
        //// screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
        useVsync(true)
        //// Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
        //// refresh rates. The Vsync setting above should limit the actual FPS to match the monitor.
        setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1)
        //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        //// useful for testing performance, but can also be very stressful to some hardware.
        //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.


        setWindowedMode(640, 960)
//        setWindowedMode(1920, 1080)
        //// You can change these files; they are in lwjgl3/src/main/resources/ .
        //// They can also be loaded from the root of assets/ .
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))

        //// This should improve compatibility with Windows machines with buggy OpenGL drivers, Macs
        //// with Apple Silicon that have to emulate compatibility with OpenGL anyway, and more.
        //// This uses the dependency `com.badlogicgames.gdx:gdx-lwjgl3-angle` to function.
        //// You can choose to remove the following line and the mentioned dependency if you want; they
        //// are not intended for games that use GL30 (which is compatibility with OpenGL ES 3.0).
        setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.ANGLE_GLES20, 0, 0)

    })
}
