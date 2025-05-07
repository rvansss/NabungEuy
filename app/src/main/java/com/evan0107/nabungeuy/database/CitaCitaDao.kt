package com.evan0107.nabungeuy.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.evan0107.nabungeuy.model.CitaCita
import kotlinx.coroutines.flow.Flow

@Dao
interface CitaCitaDao {
    @Insert
    suspend fun insert(citacita: CitaCita)

    @Update
    suspend fun update(citacita: CitaCita)

    @Query("SELECT * FROM citacita ORDER BY nama DESC")
    fun getCitaCita(): Flow<List<CitaCita>>

}