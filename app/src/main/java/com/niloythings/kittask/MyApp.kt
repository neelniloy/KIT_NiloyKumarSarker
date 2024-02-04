package com.niloythings.kittask

import android.app.Application
import com.niloythings.kittask.database.UserDatabase
import com.niloythings.kittask.repository.UserRepository

class MyApp : Application() {

    companion object {
        lateinit var database: UserDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = UserDatabase.getDatabase(this)
    }
}