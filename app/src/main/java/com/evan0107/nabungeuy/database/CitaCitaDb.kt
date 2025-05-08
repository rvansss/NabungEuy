package com.evan0107.nabungeuy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.evan0107.nabungeuy.model.CitaCita
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [CitaCita::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CitaCitaDb : RoomDatabase() {

    abstract val dao: CitaCitaDao

    companion object {

        @Volatile
        private var INSTANCE: CitaCitaDb? = null


        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context): CitaCitaDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CitaCitaDb::class.java,
                        "citaCita.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }

        }

    }
}

