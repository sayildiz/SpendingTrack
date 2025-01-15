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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject


@Entity(
    tableName = "spend"
)
data class SpendEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "amount") val amount: Double? = null,
    @ColumnInfo(name = "type") val type: String? = null,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "date") val date: Date? = Date()
)

data class Spend(
    val id: Int? = null,
    val name: String? = null,
    val amount: Double? = null,
    val type: String? = null,
    val date: Date? = Date()
)

fun SpendEntity.toSpend(): Spend {
    return Spend(
        id = this.id,
        name = this.name,
        type = this.type,
        date = this.date
    )
}

// Note: JvmName is used to provide a unique name for each extension function with the same name.
// Without this, type erasure will cause compiler errors because these methods will have the same
// signature on the JVM.
@JvmName("entityToSpend")
fun List<SpendEntity>.toSpend() = map(SpendEntity::toSpend)

fun Spend.toEntity(): SpendEntity {
    return SpendEntity(
        id = this.id,
        name = this.name,
        type = this.type,
        date = this.date
    )
}

@Dao
interface SpendDao {
    @Query("SELECT * FROM spend")
    fun getAll(): Flow<List<SpendEntity>>

    @Query("SELECT * FROM spend WHERE strftime('%Y-%m', date) = strftime('%Y-%m', 'now')")
    fun getCurrentMonth(): Flow<List<SpendEntity>>

    @Query("SELECT * FROM spend WHERE strftime('%Y-%m', date) = strftime('%Y-%m', :date)")
    suspend fun getMonth(date: String): List<SpendEntity>

    @Insert
    suspend fun insertAll(vararg spends: SpendEntity)

    @Delete
    suspend fun delete(spend: SpendEntity)
}

interface SpendRepository {
    fun getAllSpends(): Flow<List<Spend>>
    suspend fun insertAll(vararg spends: Spend)
    suspend fun delete(spend: Spend)
}

class SpendRepositoryImpl @Inject constructor(
    private val spendDao: SpendDao,
) : SpendRepository {
    override fun getAllSpends(): Flow<List<Spend>> {
        return spendDao.getAll()
            .map {
                withContext(Dispatchers.Default) {
                    it.toSpend()
                }
            }
    }

    override suspend fun insertAll(vararg spends: Spend) {
        spendDao.insertAll(*spends.map { it.toEntity() }
            .toTypedArray())
    }

    override suspend fun delete(spend: Spend) {
        spendDao.delete(spend.toEntity())
    }
}

class Converters {
    @TypeConverter
    fun fromTimeStamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}

@Database(entities = [SpendEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun spendDao(): SpendDao
}
