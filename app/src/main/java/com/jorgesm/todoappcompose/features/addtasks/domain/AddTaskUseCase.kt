package com.jorgesm.todoappcompose.features.addtasks.domain

import com.jorgesm.todoappcompose.features.addtasks.data.TaskEntity
import com.jorgesm.todoappcompose.features.addtasks.data.TaskRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    
    suspend operator fun invoke(taskEntity: TaskEntity) = taskRepository.add(taskEntity)
}