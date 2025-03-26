package com.snapbizz.core.sync

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

class SyncRepository() {
}


@Module
@InstallIn(SingletonComponent::class)
object SyncModule{

    @Provides
    @Singleton
    fun provideSyncRepo(): SyncRepository = SyncRepository()
}

//@Module
//@InstallIn(SingletonComponent::class)
//abstract class WorkerModule {
//    @Binds
//    @Singleton
//    abstract fun bindWorkerFactory(factory: HiltWorkerFactory): WorkerFactory
//}

//@Module
//@InstallIn(SingletonComponent::class)
//object WorkerModule {
//
//    @Provides
//    @Singleton
//    fun provideWorkManagerConfiguration(workerFactory: HiltWorkerFactory): Configuration {
//        return Configuration.Builder()
//            .setWorkerFactory(workerFactory)
//            .build()
//    }
//}