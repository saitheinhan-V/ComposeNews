package com.my.composenews.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.my.composenews.data.resource.NetworkResponse
import com.my.composenews.presentation.utils.NetworkHelper
import com.my.composenews.presentation.event.MainEvent
import com.my.composenews.presentation.event.MainViewModelState
import com.my.composenews.domain.usecase.MainUseCase
import com.my.composenews.domain.vo.NewsVo
import com.my.composenews.presentation.event.MainAction
import com.my.composenews.ui.theme.DayNightTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val useCase: MainUseCase
) : ViewModel(){

    private val vmEvent = MutableSharedFlow<MainEvent>()
    val uiEvent get() = vmEvent.asSharedFlow()

    private val currentPage = MutableSharedFlow<Int>()

    private val vmState = MutableStateFlow(MainViewModelState())
    val isLoading = vmState
        .map(MainViewModelState::asLoading)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.asLoading()
        )

    val newsList = vmState.map(MainViewModelState::asNews)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.asNews()
        )

    val appTheme = vmState.map(MainViewModelState::asAppTheme)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.asAppTheme()
        )
    init {
        observeNews()
        observeAppTheme()

    }

    private fun observeAppTheme(){
        viewModelScope.launch {
            useCase.getAppTheme.invoke().collectLatest {
                vmState.update { state ->
                    state.copy(
                        appTheme = it
                    )
                }
            }
        }
    }

    private fun observeNews(){
        viewModelScope.launch {
            if(!networkHelper.isNetworkConnected()){
                vmEvent.emit(MainEvent.showSnack("No internet connection!"))
                return@launch
            }


            vmState.update { state ->
                state.copy(
                    isLoading = true
                )
            }
//            useCase.getNews(1,"business").let{
//                when(it){
//                    is NetworkResponse.Failed -> {
//                        vmState.update { state ->
//                            state.copy(
//                                isLoading = false,
//                                isError = it.message
//                            )
//                        }
//                        vmEvent.emit(MainEvent.showSnack(it.message))
//                    }
//                    is NetworkResponse.Success -> {
//                        vmState.update { state ->
//                            state.copy(
//                                isLoading = false,
//                                isError = "",
//                                news = it.data.articles.map { dto -> dto.toVo(dto) }
//                            )
//                        }
//                    }
//                }
//            }
            useCase.getNews("business").collectLatest { pagingData ->
                vmState.update { state ->
                    state.copy(
                        news = flow {
                            emit(pagingData)
                        }.cachedIn(scope = viewModelScope)
                    )
                }
            }
        }
    }

    fun onActionMain(action: MainAction){
        when(action){
            is MainAction.ClickFavour -> {

            }
            is MainAction.ItemClick -> {

            }

            is MainAction.SwitchTheme -> {
                viewModelScope.launch {
                    val theme = when(action.isDark){
                        true -> DayNightTheme.NIGHT
                        false -> DayNightTheme.DAY
                    }
                    useCase.setAppTheme.invoke(theme)
                }
            }
        }
    }

}