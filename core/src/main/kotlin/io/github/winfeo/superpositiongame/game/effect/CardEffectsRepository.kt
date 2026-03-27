package io.github.winfeo.superpositiongame.game.effect

import io.github.winfeo.superpositiongame.game.effect.effect.*
import io.github.winfeo.superpositiongame.model.card.CardType

object CardEffectsRepository {
    private val effects: Map<CardType, CardEffect> = mapOf(
//        CardType.EMPTY to NoEffect(),
        CardType.PAULI to PauliEffect(),
//        CardType.ROTATE to RotateEffect(),
        CardType.PHASE to PhaseEffect(),
        CardType.HADAMARD to HadamardEffect(),
//        CardType.SWAP to SwapEffect(),
        CardType.QUANTUM_NOISE to NoiseEffect(),
        CardType.KRONECKER_MULTIPLICATION to MultiplicationEffect(),
        CardType.MEASUREMENT to MeasurementEffect(),
        CardType.IDENTITY to IdentityEffect(),
//        CardType.BARRIER to BarrierEffect(),
//        CardType.RESHAFFLE to ReshaffleEffect(),
//        CardType.QUANTUM_LUCKY to LuckyEffect()
    )

    fun getEffect(type: CardType): CardEffect? {
        return effects[type]
    }
}
