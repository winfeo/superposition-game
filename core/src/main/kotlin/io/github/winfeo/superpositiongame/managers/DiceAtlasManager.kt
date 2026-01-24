package io.github.winfeo.superpositiongame.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

object DiceAtlasManager {
    private lateinit var diceAtlas: TextureAtlas
    private val diceId: MutableSet<String> = mutableSetOf()
    private const val ASSETS_DICE_PATH = "dice/dice.atlas"

    fun loadAtlas() {
        diceAtlas = TextureAtlas(Gdx.files.internal(ASSETS_DICE_PATH))
        diceAtlas.regions.forEach { region ->
            diceId.add(region.name)
        }
    }

    fun getRegion(regionName: String): TextureRegion {
        return diceAtlas.findRegion(regionName)
    }

    fun dispose() {
        diceAtlas.dispose()
        diceId.clear()
    }
}
