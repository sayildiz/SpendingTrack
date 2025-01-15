package com.sayildiz.spendingtrack

import android.content.Context
import androidx.room.Room
import com.sayildiz.spendingtrack.model.AppDatabase
import com.sayildiz.spendingtrack.model.SpendDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideSpendDao(appDatabase: AppDatabase): SpendDao {
        return appDatabase.spendDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

}


