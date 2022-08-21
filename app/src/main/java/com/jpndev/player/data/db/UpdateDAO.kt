package com.jpndev.player.data.db

import androidx.room.*
import com.jpndev.player.data.model.Article
import com.jpndev.player.data.model.MUpdateData
import kotlinx.coroutines.flow.Flow

@Dao
interface UpdateDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(temp: MUpdateData): Long

    @Query("DELETE FROM update_table")
    suspend fun deleteUpdateTable()

    @Delete
    suspend fun delete(temp: MUpdateData) : Integer

    @Query("SELECT * FROM update_table Limit 1")
    fun getUpdateData(): MUpdateData

    @Query("SELECT * FROM update_table Limit 1")
    fun getAPPDataFromDB2(): MUpdateData
}