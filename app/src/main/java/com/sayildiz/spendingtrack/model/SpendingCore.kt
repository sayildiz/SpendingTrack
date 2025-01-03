package com.sayildiz.spendingtrack.model

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date


@Entity
data class Spend(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "value") val value: Double?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "date") val date: Date?
)

@Dao
interface SpendDao {
    @Query("SELECT * FROM spend WHERE strftime('%Y-%m', date) = strftime('%Y-%m', 'now')")
    fun getCurrentMonth(): List<Spend>

    @Query("SELECT * FROM spend WHERE strftime('%Y-%m', date) = strftime('%Y-%m', :date)")
    fun getMonth(date: String): List<Spend>

    @Insert
    fun insertAll(vararg spends: Spend)

    @Delete
    fun delete(spend: Spend)
}

class Converters {
    @TypeConverter
    fun fromTimeStamp(value: Long?): Date?{
        return value?.let{Date(it)}
    }
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time?.toLong()
    }
}

@Database(entities = [Spend::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase(){
    abstract fun spendDao(): SpendDao
}
