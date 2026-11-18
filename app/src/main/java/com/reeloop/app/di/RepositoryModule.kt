package com.reeloop.app.di

import com.reeloop.app.data.fake.FakeAuthRepository
import com.reeloop.app.data.fake.FakeObjetoRepository
import com.reeloop.app.data.local.SesionRepositoryImpl
import com.reeloop.app.data.repository.AuthRepositoryImpl
import com.reeloop.app.data.repository.ObjetoRepositoryImpl
import com.reeloop.app.domain.repository.AuthRepository
import com.reeloop.app.domain.repository.ObjetoRepository
import com.reeloop.app.domain.repository.SesionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    private const val USAR_NUBE= false
    @Provides @Singleton
     fun provideAuthRepository(remoto: AuthRepositoryImpl, fake: FakeAuthRepository): AuthRepository =
        if (USAR_NUBE) remoto else fake

    @Provides @Singleton
    fun provideObjetoRepository(remoto: ObjetoRepositoryImpl, fake: FakeObjetoRepository): ObjetoRepository =
        if (USAR_NUBE) remoto else fake

    @Provides @Singleton
    fun provideSesionRepository(impl: SesionRepositoryImpl): SesionRepository = impl
}