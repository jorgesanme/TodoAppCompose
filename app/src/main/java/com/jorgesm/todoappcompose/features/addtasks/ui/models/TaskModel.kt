package com.jorgesm.todoappcompose.features.addtasks.ui.models

data class TaskModel(
    val id: Int = System.currentTimeMillis().hashCode(),
    val taskName: String,
    var selected: Boolean = false,
    var imageString: String = ""
)
