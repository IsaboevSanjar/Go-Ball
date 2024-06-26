package goball.uz.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import goball.uz.helper.Result
import goball.uz.models.TgToken
import goball.uz.models.UserData
import goball.uz.models.staium.StadiumListItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StadiumsViewModel
@Inject constructor(private val repository: StadiumsRepository) : ViewModel() {

    private val _stadiums = MutableStateFlow<List<StadiumListItem>>(emptyList())
    val stadiums = _stadiums.asStateFlow()

    private val _login = MutableStateFlow<UserData?>(null)
    val login: StateFlow<UserData?> get() = _login

    private val _showErrorChannel = Channel<Boolean>()
    val showErrorToastChannel = _showErrorChannel.receiveAsFlow()

    private val _showErrorChannelLogin = Channel<Boolean>()
    val showErrorToastChannelLogin = _showErrorChannelLogin.receiveAsFlow()

    private val _isDataFetched = MutableStateFlow(false)


    init {
        fetchStadiums()
    }

    private fun fetchStadiums() {
        if (_isDataFetched.value) return

        viewModelScope.launch {
            repository.getStadiumsList().collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        Log.e("StadiumsViewModel", "Error fetching stadiums: ${result.data}")
                        _showErrorChannel.send(true)
                    }

                    is Result.Success -> {
                        result.data?.let { stadium ->
                            Log.d("StadiumsViewModel", "Fetched stadiums: $stadium")
                            _stadiums.update { stadium }
                            _isDataFetched.value = true // Mark data as fetched
                        } ?: Log.e("StadiumsViewModel", "Fetched stadiums data is null")
                    }
                }
            }
        }
    }

    fun loginWithTelegram(code: String) {
        viewModelScope.launch {
            repository.loginWithTelegram(code).collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        _showErrorChannelLogin.send(true)
                    }

                    is Result.Success -> {
                        _showErrorChannelLogin.send(false)
                        handleLoginSuccess(result.data)
                        result.data?.let { userData ->
                            _login.value = userData.user
                        }
                    }
                }
            }
        }
    }

    private fun handleLoginSuccess(token: TgToken?) {
        // Handle the successful login response here, such as saving the token locally or navigating to another screen.
        if (token?.token?.access_token?.isNotEmpty() == true) {
            Log.d("TELEGRAMLOGIN", "handleLoginSuccess: ${token.token.access_token}")
            Log.d("TELEGRAMLOGIN", "User: ${token.user}")
        }
    }

}