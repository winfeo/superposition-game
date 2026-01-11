package io.github.winfeo.superpositiongame.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

object CardsAtlasManager {
    private lateinit var cardsAtlas: TextureAtlas
    private const val ASSETS_CARDS_PATH = "cards/cards.atlas"

    fun loadAtlas(){
        cardsAtlas = TextureAtlas(Gdx.files.internal(ASSETS_CARDS_PATH))
    }

    fun getRegion(regionName: String): TextureRegion? {
        return cardsAtlas.findRegion(regionName)
    }

    fun getSprite(regionName: String): Sprite? {
        return cardsAtlas.createSprite(regionName)
    }

    fun dispose() = cardsAtlas.dispose()
}
