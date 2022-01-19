package com.gtera.di.module

import android.app.Application
import androidx.annotation.NonNull
import androidx.room.Room
import com.gtera.data.local.db.AppDataBase
import com.gtera.data.local.db.DataBaseCommunicator
import com.gtera.data.local.db.dao.CartDao
import com.gtera.data.local.db.dao.UserDao
import com.gtera.di.providers.ResourceProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
public class PersistenceModule {



        @Provides
        @Singleton
        fun provideAppDataBase(@NonNull application: Application): AppDataBase {
            return Room
                .databaseBuilder(application, AppDataBase::class.java, "Gtera.db")
                .build()
        }


    @Provides
    @Singleton
    fun provideDatabaseCommunicator(
        @NonNull appDataBase: AppDataBase,
        resourceProvider: ResourceProvider
    ): DataBaseCommunicator {
        return DataBaseCommunicator(appDataBase, resourceProvider)
    }


    @Provides
    @Singleton
    fun provideUserDao(@NonNull database: AppDataBase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideCartDao(@NonNull database: AppDataBase): CartDao {
        return database.cartDao()
    }


}
