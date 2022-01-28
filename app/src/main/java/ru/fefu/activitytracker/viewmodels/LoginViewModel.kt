package ru.fefu.activitytracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.activitytracker.Retrofit.LoginRepository
import ru.fefu.activitytracker.Retrofit.Result
import ru.fefu.activitytracker.data.TokenUserModel

class LoginViewModel:ViewModel() {
    private val loginRepository = LoginRepository()

    private val _dataFlow = MutableSharedFlow<Result<TokenUserModel>>(replay = 0)

    val dataFlow get() = _dataFlow

    fun login(login:String, password:String) {
        viewModelScope.launch {
            loginRepository.login(login, password)
                .collect {
                    when(it) {
                        is Result.Success<*> -> _dataFlow.emit(it as Result<TokenUserModel>)
                        is Result.Error<*> -> _dataFlow.emit(it as Result<TokenUserModel>)
                    }
                }
        }
    }
}