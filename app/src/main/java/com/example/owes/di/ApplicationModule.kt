package com.example.owes.di

import android.content.Context
import androidx.room.Room
import com.example.owes.data.Constants.DEBTOR_DATABASE_NAME
import com.example.owes.data.db.DebtorDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideDebtorDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        DebtorDatabase::class.java,
        DEBTOR_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(db: DebtorDatabase) = db.debtorDao()
}