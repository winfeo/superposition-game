package io.github.winfeo.superpositiongame.model.card.components

interface CardComponent {
    val canMove: Boolean
    val actionRadius: Int
    val requiredSpecialSlot: Boolean get() = false
}

interface MovableCardComponent {
    val axisRotation: AxisRotation
    val angleRotation: AngleRotation
}

//Компоненты?
data class EmptyComponent(
    override val canMove: Boolean = false,
    override val actionRadius: Int = 0
): CardComponent
data class PauliGateComponent(
    override val actionRadius: Int,
    override val axisRotation: AxisRotation,
    override val angleRotation: AngleRotation = AngleRotation.OPPOSITE,
    override val canMove: Boolean = true,
    override val requiredSpecialSlot: Boolean ///TODO просто по индексу карты в массиве опередлять?
): CardComponent, MovableCardComponent

data class RotateGateComponent(
    override val actionRadius: Int = 1,
    override val axisRotation: AxisRotation,
    override val angleRotation: AngleRotation = AngleRotation.ANY,
    override val canMove: Boolean = true
): CardComponent, MovableCardComponent

data class PhaseGateComponent(
    val isForwardRotation: Boolean,
    override val actionRadius: Int = 1,
    override val axisRotation: AxisRotation = AxisRotation.Z,
    override val angleRotation: AngleRotation = AngleRotation.QUARTER,
    override val canMove: Boolean = true
): CardComponent, MovableCardComponent

data class HadamardComponent(
    override val actionRadius: Int,
    override val axisRotation: AxisRotation = AxisRotation.Z,
    override val angleRotation: AngleRotation = AngleRotation.QUARTER,
    override val canMove: Boolean = true,
    override val requiredSpecialSlot: Boolean
): CardComponent, MovableCardComponent

data class SwapComponent(
    val canSwapBetweenPlayers: Boolean = true,
    override val actionRadius: Int = 0,
    override val canMove: Boolean = false
): CardComponent

data class KroneckerMultiplicationComponent(
    val additionalCards: Int = 2,
    override val actionRadius: Int = 0,
    override val canMove: Boolean = false
): CardComponent

data class BarrierComponent(
    val skipNextMove: Boolean = true,
    override val actionRadius: Int = 0,
    override val canMove: Boolean = false
): CardComponent

data class ReshuffleComponent(
    val maxCardsToReplace: Int = 4,
    override val actionRadius: Int = 0,
    override val canMove: Boolean = false
): CardComponent

data class QuantumNoiseComponent(
    override val actionRadius: Int = 1,
    override val canMove: Boolean = true
): CardComponent

data class MeasurementComponent(
    override val actionRadius: Int = 1,
    override val canMove: Boolean = true
): CardComponent

data class QuantumLuckyComponent(
    override val actionRadius: Int = 0,
    override val canMove: Boolean = false
): CardComponent

data class IdentityComponent(
    override val actionRadius: Int = 0,
    override val canMove: Boolean = false
): CardComponent
