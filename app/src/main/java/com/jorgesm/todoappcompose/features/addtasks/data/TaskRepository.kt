package com.jorgesm.todoappcompose.features.addtasks.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDAO: TaskDAO) {
    val tasks: Flow<List<TaskEntity>> = taskDAO.getAllTasks()
    
    suspend fun add(item: TaskEntity) {
        taskDAO.addTask(item)
    }
    
    suspend fun update(item: TaskEntity) {
        taskDAO.updateTask(item)
    }
    
    suspend fun delete(item: TaskEntity){
        taskDAO.deleteTask(item)
    }
}