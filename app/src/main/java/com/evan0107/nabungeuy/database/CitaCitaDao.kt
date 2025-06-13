//package com.evan0107.nabungeuy.database
//
//import androidx.room.*
//import com.evan0107.nabungeuy.model.CitaCita
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface CitaCitaDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(citaCita: CitaCita)
//
//    @Query("SELECT * FROM CitaCita")
//    fun getAllCitaCita(): Flow<List<CitaCita>>
//
//    @Query("SELECT * FROM CitaCita WHERE id = :id")
//    suspend fun getById(id: Int): CitaCita?
//
//    @Delete
//    suspend fun delete(citaCita: CitaCita)
//
//    @Update
//    suspend fun update(citaCita: CitaCita)
//
//}
