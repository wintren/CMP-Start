package com.template.app

import com.template.app.navigation.BackStackStore
import com.template.app.navigation.Navigator
import com.template.core.common.flow.StateFlowMode
import com.template.core.common.flow.combines
import com.template.core.ui.viewmodel.StateViewModel
import com.template.core.ui.viewmodel.fire
import com.template.feature.settings.contract.PreferencesRepository
import com.template.feature.settings.model.ThemeMode

/**
 * Publishes [ThemeMode], not a resolved `isDark`: "System" can only be answered inside
 * composition.
 *
 * Also the owner of session restoration, because it is the one ViewModel that lives as long as the
 * window does. [StateFlowMode.SingleStart] is what makes that safe — restoring twice would throw
 * away wherever the person had navigated to in between.
 */
internal class AppViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val navigator: Navigator,
    private val backStack: BackStackStore,
) : StateViewModel<AppViewModel.State>() {

    data class State(val themeMode: ThemeMode = ThemeMode.default)

    override fun initialState() = State()

    override val stateFlowMode = StateFlowMode.SingleStart

    override val stateFlow = viewModelState(
        data = { combines(preferencesRepository.observeThemeMode()) },
        state = { (themeMode) -> State(themeMode = themeMode) },
        onStarted = { resumeWhereWeLeftOff() },
    )

    private suspend fun resumeWhereWeLeftOff() {
        if (navigator.isAtStart) backStack.restored()?.let(navigator::restore)
        // Not awaited: the collector runs for the ViewModel's life, and `onStarted` has to return
        // for the state flow to emit at all.
        fire { navigator.navigation().collect(backStack::save) }
    }
}
