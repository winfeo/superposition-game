package io.github.winfeo.superpositiongame.model.card

import io.github.winfeo.superpositiongame.model.card.components.*

///TODO добавить описание карты?
///TODO ECS паттерн?
enum class CardType(
    val cardName: String,
    val textureId: String,
    val cardComponent: CardComponent,
) {
    EMPTY(
        cardName = "Empty Slot",
        textureId = "",
        cardComponent = EmptyComponent()
    ),
    PAULI_X(
        cardName = "Pauli Gate-X",
        textureId = "pauli_x",
        cardComponent = PauliGateComponent(
            actionRadius = 1,
            axisRotation = AxisRotation.X,
            requiredSpecialSlot = false
        )
    ),
    PAULI_Y(
        cardName = "Pauli Gate-Y",
        textureId = "pauli_y",
        cardComponent = PauliGateComponent(
            actionRadius = 1,
            axisRotation = AxisRotation.Y,
            requiredSpecialSlot = false
        )
    ),
    PAULI_Z(
        cardName = "Pauli Gate-Z",
        textureId = "pauli_z",
        cardComponent = PauliGateComponent(
            actionRadius = 1,
            axisRotation = AxisRotation.Z,
            requiredSpecialSlot = false
        )
    ),
    PAULI_X3(
        cardName = "Pauli Gate-X*3",
        textureId = "pauli_x3",
        cardComponent = PauliGateComponent(
            actionRadius = 3,
            axisRotation = AxisRotation.X,
            requiredSpecialSlot = true
        )
    ),
    PAULI_Y3(
        cardName = "Pauli Gate-Y*3",
        textureId = "pauli_y3",
        cardComponent = PauliGateComponent(
            actionRadius = 3,
            axisRotation = AxisRotation.Y,
            requiredSpecialSlot = true
        )
    ),
    PAULI_Z3(
        cardName = "Pauli Gate-Z*3",
        textureId = "pauli_z3",
        cardComponent = PauliGateComponent(
            actionRadius = 3,
            axisRotation = AxisRotation.Z,
            requiredSpecialSlot = true
        )
    ),
    ROTATE_X(
        cardName = "Rotate Gate-X",
        textureId = "rotate_x",
        cardComponent = RotateGateComponent(axisRotation = AxisRotation.X)
    ),
    ROTATE_Y(
        cardName = "Rotate Gate-Y",
        textureId = "rotate_y",
        cardComponent = RotateGateComponent(axisRotation = AxisRotation.Y)
    ),
    ROTATE_Z(
        cardName = "Rotate Gate-Z",
        textureId = "rotate_z",
        cardComponent = RotateGateComponent(axisRotation = AxisRotation.Z)
    ),
    PHASE_S(
        cardName="Phase Gate",
        textureId = "phase_s",
        cardComponent = PhaseGateComponent(isForwardRotation = true)
    ),
    PHASE_S_BACKWARDS(
        cardName="Phase Gate Backwards",
        textureId = "phase_s_backwards",
        cardComponent = PhaseGateComponent(isForwardRotation = false)
    ),
    HADAMARD_H(
        cardName = "Hadamard-H",
        textureId = "hadamard_h",
        cardComponent = HadamardComponent(
            actionRadius = 1,
            requiredSpecialSlot = false
        )
    ),
    HADAMARD_H3(
        cardName = "Hadamard-H*3",
        textureId = "hadamard_h3",
        cardComponent = HadamardComponent(
            actionRadius = 3,
            requiredSpecialSlot = false
        )
    ),

    SWAP(
        cardName = "Swap",
        textureId = "swap",
        cardComponent = SwapComponent()
    ),
    QUANTUM_NOISE(
        cardName = "Quantum noise",
        textureId = "quantum_noise",
        cardComponent = QuantumNoiseComponent()
    ),
    KRONECKER_MULTIPLICATION(
        cardName = "Kronecker multiplication",
        textureId = "kronecker_multiplication",
        cardComponent = KroneckerMultiplicationComponent()
    ),
    MEASUREMENT(
        cardName = "Measurement",
        textureId = "measurement",
        cardComponent = MeasurementComponent()
    ),
    IDENTITY(
        cardName = "Identity",
        textureId = "identity",
        cardComponent = IdentityComponent()
    ),
    BARRIER(
        cardName = "Barrier",
        textureId = "barrier",
        cardComponent = BarrierComponent()
    ),
    RESHAFFLE(
        cardName = "Reshaffle",
        textureId = "reshaffle",
        cardComponent = ReshuffleComponent()
    ),

    ///TODO нет ассета карты, удалить или добавить?
    QUANTUM_LUCKY(
        cardName = "Quantum Lucky",
        textureId = "",
        cardComponent = QuantumLuckyComponent()
    )


}
