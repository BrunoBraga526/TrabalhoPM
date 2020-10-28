package ipvc.estg.room.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ipvc.estg.room.db.CityRepository
import ipvc.estg.room.db.CityDB
import ipvc.estg.room.entities.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CityRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allCities: LiveData<List<City>>

    init {
        val citiesDao = CityDB.getDatabase(application, viewModelScope).cityDao()
        repository = CityRepository(citiesDao)
        allCities = repository.allCities
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(city: City) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(city)
    }

    // delete all
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    // delete by city
    fun deleteByCity(city: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteByCity(city)
    }

    fun getCitiesByCountry(country: String): LiveData<List<City>> {
        return repository.getCitiesByCountry(country)
    }

    fun getCountryFromCity(city: String): LiveData<City> {
        return repository.getCountryFromCity(city)
    }

    fun updateCity(city: City) = viewModelScope.launch {
        repository.updateCity(city)
    }

    fun updateCountryFromCity(city: String, country: String) = viewModelScope.launch {
        repository.updateCountryFromCity(city, country)
    }
}