package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AnimalEntity
import com.example.data.model.BookingEntity
import com.example.data.model.HealthRecordEntity
import com.example.data.model.SchemeEntity

@Database(
    entities = [
        AnimalEntity::class,
        BookingEntity::class,
        HealthRecordEntity::class,
        SchemeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun animalDao(): AnimalDao
    abstract fun bookingDao(): BookingDao
    abstract fun healthRecordDao(): HealthRecordDao
    abstract fun schemeDao(): SchemeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vetsathi_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
