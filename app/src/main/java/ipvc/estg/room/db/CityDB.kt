package ipvc.estg.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ipvc.estg.room.dao.CityDao
import ipvc.estg.room.entities.City
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the City class

// Note: When you modify the database schema, you'll need to update the version number and define a migration strategy
//For a sample, a destroy and re-create strategy can be sufficient. But, for a real app, you must implement a migration strategy.

@Database(entities = arrayOf(City::class), version = 8, exportSchema = false)
public abstract class CityDB : RoomDatabase() {

    abstract fun cityDao(): CityDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var cityDao = database.cityDao()

                    // Delete all content here.
                    cityDao.deleteAll()

                    // Add sample cities.
                    var city = City(1, "Viana do Castelo", "Portugal")
                    cityDao.insert(city)
                    city = City(2, "Braga", "Portugal")
                    cityDao.insert(city)
                    city = City(3, "Aveiro", "Portugal")
                    cityDao.insert(city)

                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: CityDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CityDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CityDB::class.java,
                    "cities_database"
                )
                //estratégia de destruição
                .fallbackToDestructiveMigration()
                .addCallback(WordDatabaseCallback(scope))
                .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}