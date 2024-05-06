package com.jorgesm.todoappcompose.features.addtasks.ui

import com.jorgesm.todoappcompose.features.addtasks.ui.models.TaskModel

sealed interface TasksUiState {
    object Loading:TasksUiState
    data class Error(val throwable: Throwable):TasksUiState
    data class Success(val tasks: List<TaskModel>):TasksUiState
}