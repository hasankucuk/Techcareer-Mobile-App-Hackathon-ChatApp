package com.techcareer.mobileapphackathon.chatapp.di.repository

import com.google.firebase.auth.FirebaseAuth
import com.techcareer.mobileapphackathon.chatapp.repository.login.FirebaseAuthRepository
import com.techcareer.mobileapphackathon.chatapp.repository.login.FirebaseAuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseAuthModule {

    @Provides
    @Singleton
    internal fun provideFirebaseAuthRepository(firebaseAuth: FirebaseAuth): FirebaseAuthRepository =
        FirebaseAuthRepositoryImpl(firebaseAuth)
}