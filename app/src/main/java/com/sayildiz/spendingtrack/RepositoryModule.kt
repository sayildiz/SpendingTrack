package com.sayildiz.spendingtrack

import com.sayildiz.spendingtrack.model.SpendRepository
import com.sayildiz.spendingtrack.model.SpendRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule{
    @Binds
    abstract fun bindSpendRepository(impl: SpendRepositoryImpl): SpendRepository
}

