package ipvc.estg.room.db

import androidx.lifecycle.LiveData
import ipvc.estg.room.dao.CityDao
import ipvc.estg.room.entities.City

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class CityRepository(private val cityDao: CityDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allCities: LiveData<List<City>> = cityDao.getAllCities()

    fun getCitiesByCountry(country: String): LiveData<List<City>> {
        return cityDao.getCitiesByCountry(country)
    }

    fun getCountryFromCity(city: String): LiveData<City> {
        return cityDao.getCountryFromCity(city)
    }

    suspend fun insert(city: City) {
        cityDao.insert(city)
    }

    suspend fun deleteAll(){
        cityDao.deleteAll()
    }

    suspend fun deleteByCity(city: String){
        cityDao.deleteByCity(city)
    }

    suspend fun updateCity(city: City) {
        cityDao.updateCity(city)
    }

    suspend fun updateCountryFromCity(city: String, country: String){
        cityDao.updateCountryFromCity(city, country)
    }
}