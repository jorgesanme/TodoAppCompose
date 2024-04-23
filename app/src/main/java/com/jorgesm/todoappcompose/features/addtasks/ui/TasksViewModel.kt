package com.jorgesm.todoappcompose.features.addtasks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgesm.todoappcompose.features.addtasks.domain.transformToDDBB
import com.jorgesm.todoappcompose.features.addtasks.domain.AddTaskUseCase
import com.jorgesm.todoappcompose.features.addtasks.domain.DeleteTaskUseCase
import com.jorgesm.todoappcompose.features.addtasks.domain.GetTasksUseCase
import com.jorgesm.todoappcompose.features.addtasks.domain.UpdateTaskUseCase
import com.jorgesm.todoappcompose.features.addtasks.domain.transformToDomainList
import com.jorgesm.todoappcompose.features.addtasks.ui.TasksUiState.Error
import com.jorgesm.todoappcompose.features.addtasks.ui.TasksUiState.Success
import com.jorgesm.todoappcompose.features.addtasks.ui.models.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    getTasksUseCase: GetTasksUseCase
) : ViewModel() {
    
    val uiState: StateFlow<TasksUiState> = getTasksUseCase().map { list ->
        Success(list.transformToDomainList())
    }
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TasksUiState.Loading)
        
    
    private val _showDialog = MutableStateFlow<Boolean>(false)
    val showDialog: StateFlow<Boolean> get() = _showDialog
    
    private val _editItem = MutableStateFlow<TaskModel>(TaskModel(taskName = ""))
    val editItem: StateFlow<TaskModel> get() = _editItem
    
    private val _editDialog = MutableStateFlow<Boolean>(false)
    val editDialog: StateFlow<Boolean> get() = _editDialog
    
    
    fun onDialogClose() {
        viewModelScope.launch {
            _showDialog.value = false
        }
    }
    fun onEditDialogOpen(){
        viewModelScope.launch { _editDialog.value = true }
    }
    
    fun onEditMode(item: TaskModel){
        _editItem.value = item
    }
    
    fun onEditDialogClose(){
        viewModelScope.launch { _editDialog.value = false }
    }
    
    fun onTasksCreated(task: String) {
        onDialogClose()
        viewModelScope.launch(Dispatchers.IO) {
            addTaskUseCase(TaskModel(taskName = task).transformToDDBB())
        }
    }
    
    fun onShowDialogClick() {
        _showDialog.value = true
    }
    
    fun onCheckBoxSelected(item: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTaskUseCase(item.copy(selected = !item.selected).transformToDDBB())
        }
    }
    fun onItemUpdate(item: TaskModel, newText: String){
        viewModelScope.launch(Dispatchers.IO) {
            updateTaskUseCase(item.copy(taskName = newText).transformToDDBB())
        }
        onEditDialogClose()
    }
    
    fun onItemRemove(item: TaskModel) {
        viewModelScope.launch() {
            deleteTaskUseCase(item.transformToDDBB())
        }
    }
}
