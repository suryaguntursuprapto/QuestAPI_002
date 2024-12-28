package com.example.mysql_20220140002

import android.app.Application
import com.example.mysql_20220140002.container.AppContainer
import com.example.mysql_20220140002.container.MahasiswaContainer

class MahasiswaApplications:Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container=MahasiswaContainer()
    }
}