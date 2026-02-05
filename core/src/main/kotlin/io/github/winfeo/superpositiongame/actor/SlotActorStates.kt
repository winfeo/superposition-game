package io.github.winfeo.superpositiongame.actor

enum class SlotActorStates {
    NO_ACTION,
    HOVERED_CAN_PLACE,
    HOVERED_CANT_PLACE,
    REQUIRED_DICE_STATE
    //FROZEN_ONE_MOVE ///TODO добавить состояние при котором карту нельзя изменить на один ход?
}
