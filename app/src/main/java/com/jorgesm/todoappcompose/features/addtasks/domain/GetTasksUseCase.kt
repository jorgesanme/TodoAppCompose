package com.jorgesm.todoappcompose.features.addtasks.domain

import com.jorgesm.todoappcompose.features.addtasks.data.TaskEntity
import com.jorgesm.todoappcompose.features.addtasks.data.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke():Flow<List<TaskEntity>>{ return taskRepository.tasks}
}