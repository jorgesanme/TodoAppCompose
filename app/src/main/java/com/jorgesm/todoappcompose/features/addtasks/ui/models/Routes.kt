package com.jorgesm.todoappcompose.features.addtasks.ui.models

sealed class Routes(val route: String) {
    object TaskScreen:Routes("TasksScreen")
    object PhotoTaker:Routes("PhotoTaker/{item}")
}