package com.example.application.di

import com.example.application.firebase.FirebaseCommon
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFireBaseAuth()= FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore()=FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFireBaseCommon(firestore: FirebaseFirestore,auth: FirebaseAuth)=FirebaseCommon(firestore,auth)

}
