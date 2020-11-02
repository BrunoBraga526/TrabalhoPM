package ipvc.estg.room.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ipvc.estg.room.db.NotaRepository
import ipvc.estg.room.db.NotaDB
import ipvc.estg.room.entities.Nota
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotaRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allNotas: LiveData<List<Nota>>

    init {
        val citiesDao = NotaDB.getDatabase(application, viewModelScope).notaDao()
        repository = NotaRepository(citiesDao)
        allNotas = repository.allNotas
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(nota: Nota) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(nota)
    }

    // delete all
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    // delete by city
    fun deleteByNota(nota: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteByNota(nota)
    }

    fun getNotaByTexto(country: String): LiveData<List<Nota>> {
        return repository.getNotasByTexto(country)
    }

    fun getTextoFromNota(nota: String): LiveData<Nota> {
        return repository.getTextoFromNota(nota)
    }

    fun updateNota(nota: Nota) = viewModelScope.launch {
        repository.updateNota(nota)
    }

    fun updateTextoFromNota(nota: String, texto: String) = viewModelScope.launch {
        repository.updateTextoFromNota(nota, texto)
    }
}