package goball.uz.screens.mystadium

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedLatLonViewModel : ViewModel() {

    private val _selectedLatitude = MutableStateFlow(0.0)
    val selectedLatitude: StateFlow<Double> get() = _selectedLatitude

    private val _selectedLongitude = MutableStateFlow(0.0)
    val selectedLongitude: StateFlow<Double> get() = _selectedLongitude

    private val _locationIsPicked = MutableStateFlow(false)
    val locationIsPicked: StateFlow<Boolean> get() = _locationIsPicked

    fun setLocation(latitude: Double, longitude: Double) {
        _selectedLatitude.value = latitude
        _selectedLongitude.value = longitude
        _locationIsPicked.value = true
    }
}