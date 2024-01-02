package com.example.application.di

import android.app.Application
import android.content.Context
import android.view.Display.FLAG_PRIVATE
import android.view.Display.Mode
import com.example.application.firebase.FirebaseCommon
import com.example.application.util.Constants.INTRODUCTION_SP
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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

    @Provides
    fun provideIntroductionSp(
        application: Application
    )=application.getSharedPreferences(INTRODUCTION_SP, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideStorage()=FirebaseStorage.getInstance().reference

}
