package com.example.game.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface GameDao {

    @Query("SELECT * from game ORDER BY name ASC")
    fun getGames(): Flow<List<Game>>

    @Query("SELECT * from game WHERE id = :id")
    fun getDame(id: Int): Flow<Game>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: Game)

    @Update
    suspend fun update(game: Game)

    @Delete
    suspend fun delete(game: Game)
}
