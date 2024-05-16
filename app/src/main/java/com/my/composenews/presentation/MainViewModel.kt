package com.my.composenews.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.composenews.data.resource.NetworkResponse
import com.my.composenews.presentation.utils.NetworkHelper
import com.my.composenews.presentation.event.MainEvent
import com.my.composenews.presentation.event.MainViewModelState
import com.my.composenews.domain.usecase.MainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
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

    init {
        observeNews()
    }

    private fun observeNews(){
        viewModelScope.launch {
            if(!networkHelper.isNetworkConnected()){
                vmEvent.emit(MainEvent.showSnack("No internet connection!"))
                return@launch
            }
            useCase.getNews(1,"business").let{
                when(it){
                    is NetworkResponse.Failed -> {
                        vmState.update { state ->
                            state.copy(
                                isLoading = true
                            )
                        }
                        vmEvent.emit(MainEvent.showSnack(it.message))
                    }
                    is NetworkResponse.Success -> {
                        vmState.update { state ->
                            state.copy(
                                isLoading = false,
                                news = it.data.articles.map { dto -> dto.toVo(dto) }
                            )
                        }
                    }
                }
            }
        }
    }

//    fun getNews(pageNumber: Int,category: String) {
//        viewModelScope.launch {
//            _news.postValue(Resource.loading(null))
//            if (networkHelper.isNetworkConnected()) {
//                authApiRepository.getNews(pageNumber, category).let {
//                    if (it.isSuccessful) {
//                        _news.postValue(Resource.success(it.body()?.articles))
//                    } else _news.postValue(Resource.error(it.errorBody().toString(), null))
//                }
//            } else _news.postValue(Resource.error("No internet connection", null))
//        }
//    }


}