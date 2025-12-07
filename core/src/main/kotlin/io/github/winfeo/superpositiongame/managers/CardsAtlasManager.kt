package io.github.winfeo.superpositiongame.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

object CardsAtlasManager {
    private lateinit var cardsAtlas: TextureAtlas

    fun loadAtlas(path: String){
        cardsAtlas = TextureAtlas(Gdx.files.internal(path))
    }

    fun getRegion(regionName: String): TextureRegion? {
        return cardsAtlas.findRegion(regionName)
    }

    fun getSprite(regionName: String): Sprite? {
        return cardsAtlas.createSprite(regionName)
    }

    fun dispose() = cardsAtlas.dispose()
}
