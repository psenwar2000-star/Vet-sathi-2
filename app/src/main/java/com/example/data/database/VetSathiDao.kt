package com.example.data.database

import androidx.room.*
import com.example.data.model.AnimalEntity
import com.example.data.model.BookingEntity
import com.example.data.model.HealthRecordEntity
import com.example.data.model.SchemeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalDao {
    @Query("SELECT * FROM animals ORDER BY id DESC")
    fun getAllAnimals(): Flow<List<AnimalEntity>>

    @Query("SELECT * FROM animals WHERE id = :id")
    suspend fun getAnimalById(id: Long): AnimalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimal(animal: AnimalEntity): Long

    @Delete
    suspend fun deleteAnimal(animal: AnimalEntity)
}

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY id DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE id = :id")
    suspend fun getBookingById(id: Long): BookingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity): Long

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    @Query("UPDATE bookings SET status = :status WHERE id = :id")
    suspend fun updateBookingStatus(id: Long, status: String)
}

@Dao
interface HealthRecordDao {
    @Query("SELECT * FROM health_records ORDER BY id DESC")
    fun getAllRecords(): Flow<List<HealthRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: HealthRecordEntity): Long
}

@Dao
interface SchemeDao {
    @Query("SELECT * FROM schemes")
    fun getAllSchemes(): Flow<List<SchemeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchemes(schemes: List<SchemeEntity>)
}
