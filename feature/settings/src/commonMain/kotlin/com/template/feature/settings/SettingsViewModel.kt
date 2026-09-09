package com.template.feature.settings

import com.template.core.common.flow.combines
import com.template.core.ui.resource.StringValue
import com.template.core.ui.viewmodel.StateViewModel
import com.template.core.ui.viewmodel.WithActions
import com.template.core.ui.viewmodel.fire
import com.template.feature.settings.SettingsModels.Action
import com.template.feature.settings.SettingsModels.State
import com.template.feature.settings.contract.PreferencesRepository
import com.template.feature.settings.model.ThemeMode
import com.template.feature.settings.model.UnitSystem

/**
 * The canonical ViewModel shape: `initialState`, one derived `stateFlow`, one `onAction`.
 *
 * Both repository reads here are *simple access* — plain accessors used as-is in state — which is
 * the one case a ViewModel may talk to a repository directly. Wrapping either in a UseCase that
 * only forwarded the call would add a name and nothing else.
 */
class SettingsViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val onBack: () -> Unit,
) : StateViewModel<State>(), WithActions<Action> {

    override fun initialState() = State()

    override val stateFlow = viewModelState(
        data = {
            combines(
                preferencesRepository.observeUnitSystem(),
                preferencesRepository.observeThemeMode(),
            )
        },
        state = { (unitSystem, themeMode) ->
            State(
                unitSystem = unitSystem,
                themeMode = themeMode,
                temperatureExample = StringValue.Raw(exampleFor(unitSystem)),
            )
        },
    )

    override fun onAction(action: Action) {
        when (action) {
            Action.OnBack -> onBack()
            is Action.OnUnitSystemChange -> fire {
                preferencesRepository.setUnitSystem(action.unitSystem)
            }
            is Action.OnThemeModeChange -> fire {
                preferencesRepository.setThemeMode(action.themeMode)
            }
        }
    }

    private fun exampleFor(unitSystem: UnitSystem): String = when (unitSystem) {
        UnitSystem.Metric -> "21°C · 4 m/s · 2 mm"
        UnitSystem.Imperial -> "70°F · 9 mph · 0.1 in"
    }
}
