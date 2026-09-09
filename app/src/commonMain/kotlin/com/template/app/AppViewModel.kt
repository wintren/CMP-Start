package com.template.app

import com.template.core.common.flow.combines
import com.template.core.ui.viewmodel.StateViewModel
import com.template.feature.settings.contract.PreferencesRepository
import com.template.feature.settings.model.ThemeMode

/**
 * Publishes [ThemeMode], not a resolved `isDark`: "System" can only be answered inside
 * composition.
 */
class AppViewModel(
    private val preferencesRepository: PreferencesRepository,
) : StateViewModel<AppViewModel.State>() {

    data class State(val themeMode: ThemeMode = ThemeMode.default)

    override fun initialState() = State()

    override val stateFlow = viewModelState(
        data = { combines(preferencesRepository.observeThemeMode()) },
        state = { (themeMode) -> State(themeMode = themeMode) },
    )
}
