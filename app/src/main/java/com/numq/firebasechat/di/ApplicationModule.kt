package com.numq.firebasechat.di

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
    fun provideFirebaseApp(@ApplicationContext context: Context): FirebaseApp {
        FirebaseApp.initializeApp(context)
        return FirebaseApp.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(app: FirebaseApp) = FirebaseAuth.getInstance(app)

    @Provides
    @Singleton
    fun provideFirestore(app: FirebaseApp) = FirebaseFirestore.getInstance(app)

    @Provides
    @Singleton
    fun provideFirebaseStorage(app: FirebaseApp) = FirebaseStorage.getInstance(app)

}
