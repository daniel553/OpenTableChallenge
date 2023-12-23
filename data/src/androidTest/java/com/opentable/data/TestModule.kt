package com.opentable.data

import android.content.Context
import androidx.room.Room
import com.opentable.data.db.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

/**
 * 💡DI for instrumented tests
 */
@Module
@InstallIn(SingletonComponent::class)
internal object TestModule {

    @Provides
    @Named("test-opentable-challenge.db")
    fun provideTestDatabase(@ApplicationContext context: Context) = Room
        .inMemoryDatabaseBuilder(context = context, Database::class.java)
        .allowMainThreadQueries() //💡to execute queries in the main thread
        .build()
}