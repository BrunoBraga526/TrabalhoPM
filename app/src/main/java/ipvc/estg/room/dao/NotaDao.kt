package ipvc.estg.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.room.entities.Nota

@Dao
interface NotasDao {

    @Query("SELECT * from nota_table ORDER BY nota ASC")
    fun getAllNotas(): LiveData<List<Nota>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Nota)

    @Update
    suspend fun updateNota(nota: Nota)

    @Query("DELETE FROM nota_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteNota(nota: Nota)

}