package goball.uz.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import goball.uz.helper.Result
import goball.uz.models.TgToken
import goball.uz.models.staium.StadiumListItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StadiumsViewModel
@Inject constructor(private val repository: StadiumsRepository) : ViewModel() {

    private val _stadiums = MutableStateFlow<List<StadiumListItem>>(emptyList())
    val stadiums = _stadiums.asStateFlow()

    private val _showErrorChannel = Channel<Boolean>()
    val showErrorToastChannel = _showErrorChannel.receiveAsFlow()


    init {
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
                        }?:Log.e("StadiumsViewModel", "Fetched stadiums data is null")
                    }
                }
            }
        }
    }

    fun loginWithTelegram(code: Int) {
        viewModelScope.launch {
            repository.loginWithTelegram(code).collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        _showErrorChannel.send(true)
                    }

                    is Result.Success -> {
                        handleLoginSuccess(result.data)
                    }
                }
            }
        }
    }

    private fun handleLoginSuccess(token: TgToken?) {
        // Handle the successful login response here, such as saving the token locally or navigating to another screen.
        if (token?.token?.access_token?.isNotEmpty() == true) {
            Log.d("TELEGRAMLOGIN", "handleLoginSuccess: ${token.token.access_token}")
        }
    }

}