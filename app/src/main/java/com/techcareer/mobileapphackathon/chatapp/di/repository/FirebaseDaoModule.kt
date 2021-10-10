package com.techcareer.mobileapphackathon.chatapp.di.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.techcareer.mobileapphackathon.chatapp.repository.home.FirebaseDaoRepository
import com.techcareer.mobileapphackathon.chatapp.repository.home.FirebaseDaoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseDaoModule {

    @Provides
    @Singleton
    internal fun provideFirebaseDaoRepository(
        firebaseFirestore: FirebaseFirestore,
        firebaseRemoteConfig: FirebaseRemoteConfig
    ): FirebaseDaoRepository =
        FirebaseDaoRepositoryImpl(firebaseFirestore, firebaseRemoteConfig)

}