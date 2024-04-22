package com.jorgesm.todoappcompose.features.addtasks.ui.models

data class TaskModel(
    val id: Long = System.currentTimeMillis(),
    val taskName: String,
    var selected: Boolean = false
)
