package ipvc.estg.room.db

import androidx.lifecycle.LiveData
import ipvc.estg.room.dao.NotasDao
import ipvc.estg.room.entities.Nota
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class NotaRepository(private val notaDao: NotasDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allNotas: LiveData<List<Nota>> = notaDao.getAllNotas()

    fun getNotasByTexto(texto: String): LiveData<List<Nota>> {
        return notaDao.getNotasByTexto(texto)
    }

    fun getTextoFromNota(nota: String): LiveData<Nota> {
        return notaDao.getTextoFromNota(nota)
    }
    suspend fun deleteById(id: Int) {
            notaDao.deleteById(id)
        }
    suspend fun insert(nota: Nota) {
        notaDao.insert(nota)
    }

    suspend fun deleteAll(){
        notaDao.deleteAll()
    }

    suspend fun deleteByNota(nota: String){
        notaDao.deleteByNota(nota)
    }

    suspend fun updateNota(nota: Nota) {
        notaDao.updateNota(nota)
    }

    suspend fun updateTextoFromNota(nota: String, texto: String){
        notaDao.updateTextoFromNota(nota, texto)
    }
}