package com.example.myguitartuner.c_dependecy_injections_katmani.a_tuner

import com.example.myguitartuner.a_domain_katmani.b_repository.ITunerRepository
import com.example.myguitartuner.b_data_katmani.g_repository_dataHarmanlayicisi.TunerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindTunerRepo(impl: TunerRepositoryImpl): ITunerRepository
}