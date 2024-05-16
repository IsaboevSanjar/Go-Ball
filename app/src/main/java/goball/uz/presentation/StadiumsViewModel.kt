package goball.uz.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import goball.uz.helper.Result
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
                        _showErrorChannel.send(true)
                    }

                    is Result.Success -> {
                        result.data?.let { stadium ->
                            _stadiums.update { stadium }
                        }
                    }
                }
            }
        }
    }
}