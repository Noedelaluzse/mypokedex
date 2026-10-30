package com.noedelaluz.mypokedex

import android.app.Application
import com.noedelaluz.mypokedex.di.AppContainer

class MyApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
