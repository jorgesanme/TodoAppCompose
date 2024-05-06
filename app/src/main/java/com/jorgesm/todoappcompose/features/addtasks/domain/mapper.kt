package com.jorgesm.todoappcompose.features.addtasks.domain

import com.jorgesm.todoappcompose.features.addtasks.data.TaskEntity
import com.jorgesm.todoappcompose.features.addtasks.ui.models.TaskModel

fun TaskModel.transformToDDBB(): TaskEntity = TaskEntity(
    id = this.id,
    taskName = this.taskName,
    selected = this.selected,
    imageString= this.imageString
)

fun TaskEntity.transformToDomain(): TaskModel = TaskModel(
    id = this.id,
    taskName = this.taskName,
    selected = this.selected,
    imageString= this.imageString
)

fun List<TaskModel>.transformToDDBBList(): List<TaskEntity> {
    return this.map { it.transformToDDBB() }
}

fun List<TaskEntity>.transformToDomainList(): List<TaskModel>{
    return this.map { it.transformToDomain() }
}
