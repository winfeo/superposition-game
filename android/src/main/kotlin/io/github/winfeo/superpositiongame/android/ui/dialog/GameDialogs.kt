package io.github.winfeo.superpositiongame.android.ui.dialog

import io.github.winfeo.superpositiongame.android.ui.screen.game.GameViewModel
import io.github.winfeo.superpositiongame.graphics.Dialogs
import io.github.winfeo.superpositiongame.model.dice.DiceState

class GameDialogs(
    private val viewModel: GameViewModel
): Dialogs {

    override fun showRotateCardDialog(
        availableStates: List<DiceState>,
        onStateSelected: (DiceState) -> Unit
    ) {
        viewModel.showRotateCardDialog(
            availableStates = availableStates,
            onStateSelected = onStateSelected
        )


    }
//        activity.runOnUiThread {
//            val dialog = Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
//
//            // Создаем LifecycleOwner для ComposeView
//            val lifecycleOwner = DialogLifecycleOwner()
//            val savedStateRegistryOwner = DialogSavedStateRegistryOwner()
//
//            val composeView = ComposeView(activity).apply {
//                // Устанавливаем владельцев для Compose
//                setViewTreeLifecycleOwner(lifecycleOwner)
//                setViewTreeSavedStateRegistryOwner(savedStateRegistryOwner)
//
//                setContent {
//                    SuperpositionGameTheme {
//                        RotateCardDialog(
//                            availableStates = availableStates,
//                            onStateSelected = { state ->
//                                onStateSelected(state)
//                                lifecycleOwner.destroy()
//                                dialog.dismiss()
//                            }
//                        )
//                    }
//                }
//            }
//
//            lifecycleOwner.start()
//            dialog.setContentView(composeView)
//            dialog.show()
//
//            dialog.setOnDismissListener {
//                lifecycleOwner.destroy()
//            }
//        }
//
//        // Обновляем состояние ViewModel, что вызовет показ диалога в Compose
//        viewModel.requestRotateDialog(availableStates, onStateSelected)
//    }
}
