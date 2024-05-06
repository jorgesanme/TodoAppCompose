package com.jorgesm.todoappcompose.features.addtasks.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAO {
    
    @Query("SELECT * FROM TaskEntity")
    fun getAllTasks(): Flow<List<TaskEntity>>
    
    @Insert()
    suspend fun addTask(item: TaskEntity)
    @Update
    suspend fun updateTask(item: TaskEntity)
    
    @Delete
    suspend fun deleteTask(item: TaskEntity)
}