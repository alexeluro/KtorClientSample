package com.example.ktorclientsample.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ktorclientsample.data.Repository
import com.example.ktorclientsample.data.RepositoryImpl
import com.example.ktorclientsample.domain.data.User
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel constructor(
    private val repository: Repository
) : ViewModel() {

    sealed class UIState() {
        object Loading : UIState()
        data class Success(val data: List<User>) : UIState()
        data class Failure(val message: String) : UIState()
    }

    private var _getUser = Channel<UIState>(Channel.BUFFERED)
    val getUserState: Flow<UIState>
        get() = _getUser.receiveAsFlow()

    fun getUser() {
        viewModelScope.launch {
            _getUser.send(UIState.Loading)
            when (val response = repository.getUsers()) {
                is RepositoryImpl.NetworkEvents.Success -> {
                    _getUser.send(UIState.Success(response.data))
                }
                is RepositoryImpl.NetworkEvents.Failure -> {
                    _getUser.send(UIState.Failure(response.message))
                }
            }
        }
    }

    init {
        getUser()
    }

}