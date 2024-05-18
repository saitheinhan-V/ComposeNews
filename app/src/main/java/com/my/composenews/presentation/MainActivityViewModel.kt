package com.my.composenews.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.composenews.domain.usecase.GetAppTheme
import com.my.composenews.presentation.event.MainActivityViewModelState
import com.my.composenews.ui.theme.AppThemeStatus
import com.my.composenews.ui.theme.DayNightTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getTheme: GetAppTheme
): ViewModel() {

    private val vmState = MutableStateFlow(MainActivityViewModelState())
    val appTheme = vmState
        .map(MainActivityViewModelState::asAppTheme)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.asAppTheme()
        )

    init {
        getAppTheme()
    }

    private fun getAppTheme() {
        viewModelScope.launch {
            getTheme.invoke().collectLatest {
                vmState.update { state ->
                    state.copy(
                        appTheme = it
                    )
                }
            }
        }
    }

}