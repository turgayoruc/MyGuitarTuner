package com.example.myguitartuner.b_data_katmani.b_local_ya_da_database.c_database

import TunerDao
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myguitartuner.b_data_katmani.e_source.TunerDefaults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.myguitartuner.b_data_katmani.b_local_ya_da_database.a_entity.TunerEntity
import com.example.myguitartuner.b_data_katmani.f_mapper_donusturuculer.toEntity

@Database(entities = [TunerEntity::class], version = 1)
abstract class TunerDatabase : RoomDatabase() {
    abstract fun noteDao(): TunerDao

    companion object {
        @Volatile
        private var INSTANCE: TunerDatabase? = null

        //Bu metodun amaci: Uygulama ilk acildiginda SQLite varsa ondaki verileri Kotlin'e ceviriyor.
        //Yoksa da ilk SQLite dosyasini "assets" icine olusturacak ve yine istedigin degiskene Kotline donusmus halini verecek.
        fun getDatabase(context: Context, scope: CoroutineScope): TunerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TunerDatabase::class.java,
                    "note_database"
                                                   )
                    .addCallback(NoteDatabaseCallback(scope)) // Verileri ilk başta yüklemek için
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    //Sadece uygulama ilk acildiginda varsa kullanman gereken veri onlari SQLite icine kaydedecek
    private class NoteDatabaseCallback(private val scope: CoroutineScope) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    val dao = database.noteDao()
                    // Tüm listelerinizi buraya ekleyin
                    val initialString1: List<TunerEntity> = TunerDefaults.initialString1.map { it.toEntity() }
                    val initialString2: List<TunerEntity> = TunerDefaults.initialString2.map { it.toEntity() }
                    val initialString3: List<TunerEntity> = TunerDefaults.initialString3.map { it.toEntity() }
                    val initialString4: List<TunerEntity> = TunerDefaults.initialString4.map { it.toEntity() }
                    val initialString5: List<TunerEntity> = TunerDefaults.initialString5.map { it.toEntity() }
                    val initialString6: List<TunerEntity> = TunerDefaults.initialString6.map { it.toEntity() }
                    val initialStringDefault: List<TunerEntity> =
                        TunerDefaults.initialStringDefault.map { it.toEntity() }
                    dao.insertNotes(initialString1)
                    dao.insertNotes(initialString2)
                    dao.insertNotes(initialString3)
                    dao.insertNotes(initialString4)
                    dao.insertNotes(initialString5)
                    dao.insertNotes(initialString6)
                    dao.insertNotes(initialStringDefault)
                }
            }
        }
    }
}