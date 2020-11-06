package ipvc.estg.room.db

import androidx.lifecycle.LiveData
import ipvc.estg.room.dao.NotasDao
import ipvc.estg.room.entities.Nota

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class NotaRepository(private val notaDao: NotasDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allNotas: LiveData<List<Nota>> = notaDao.getAllNotas()

    suspend fun insert(nota: Nota) {
        notaDao.insert(nota)
    }

    suspend fun deleteAll(){
        notaDao.deleteAll()
    }

    suspend fun updateNota(nota: Nota) {
        notaDao.updateNota(nota)
    }
    suspend fun deleteNota( nota: Nota) {
        notaDao.deleteNota (nota)
    }
}