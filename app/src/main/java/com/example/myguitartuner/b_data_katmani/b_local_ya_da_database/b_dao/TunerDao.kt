import androidx.room.*
import com.example.myguitartuner.b_data_katmani.b_local_ya_da_database.a_entity.TunerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TunerDao {
    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<TunerEntity>>

    @Query("SELECT * FROM notes WHERE telAdi = :stringName")
    fun getNotesByString(stringName: String): Flow<List<TunerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<TunerEntity>)

    @Update
    suspend fun updateNotes(notes: List<TunerEntity>)

    @Query("DELETE FROM notes")
    suspend fun clearAll()
}