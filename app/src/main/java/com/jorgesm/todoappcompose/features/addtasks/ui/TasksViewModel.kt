package com.jorgesm.todoappcompose.features.addtasks.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgesm.todoappcompose.features.addtasks.ui.models.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor() : ViewModel() {
    
    private val _showDialog = MutableStateFlow<Boolean>(false)
    val showDialog: StateFlow<Boolean> get() = _showDialog
    
    private val _tasksList = mutableStateListOf<TaskModel>()
    val tasksList: List<TaskModel> get() = _tasksList
    
    fun onDialogClose() {
        viewModelScope.launch {
            _showDialog.value = false
        }
    }
    
    fun onTasksCreated(task: String) {
        Log.i("yo", task)
        _tasksList.add(TaskModel(taskName = task))
        onDialogClose()
    }
    
    fun onShowDialogClick() {
        _showDialog.value = true
    }
    
    fun onCheckBoxSelected(item: TaskModel) {
        val index = _tasksList.indexOf(item)
        _tasksList[index] = _tasksList[index].let {
            it.copy(selected = !it.selected)
        }
    }
    
    fun onItemRemove(item: TaskModel) {
        val task = _tasksList.find { it.id == item.id }
        _tasksList.remove(task)
    }
}
