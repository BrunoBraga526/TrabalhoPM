package ipvc.estg.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.room.entities.Nota

@Dao
interface NotasDao {

    @Query("SELECT * from nota_table ORDER BY nota ASC")
    fun getAllNotas(): LiveData<List<Nota>>

    @Query("SELECT * FROM nota_table WHERE texto == :texto")
    fun getNotasByTexto(texto: String): LiveData<List<Nota>>

    @Query("SELECT * FROM nota_table WHERE nota == :nota")
    fun getTextoFromNota(nota: String): LiveData<Nota>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Nota)

    @Update
    suspend fun updateNota(nota: Nota)

    @Query("DELETE FROM nota_table")
    suspend fun deleteAll()

    @Query("DELETE FROM nota_table where nota == :nota")
    suspend fun deleteByNota(nota: String)

    @Query("UPDATE nota_table SET texto=:texto WHERE nota == :nota")
    suspend fun updateTextoFromNota(nota: String, texto: String)
}