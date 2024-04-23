package com.jorgesm.todoappcompose.features.addtasks.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.jorgesm.todoappcompose.features.addtasks.ui.models.TaskModel


@Composable
fun TasksScreen(tasksViewModel: TasksViewModel) {
    val showDialog: Boolean by tasksViewModel.showDialog.collectAsStateWithLifecycle()
    val editDialog: Boolean by tasksViewModel.editDialog.collectAsStateWithLifecycle()
    val editItem: TaskModel by tasksViewModel.editItem.collectAsStateWithLifecycle()
    val livecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<TasksUiState>(
        initialValue = TasksUiState.Loading,
        key1 = livecycle,
        key2 = tasksViewModel
    ) {
        livecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            tasksViewModel.uiState.collect { value = it }
        }
    }
    when (uiState) {
        is TasksUiState.Error -> {}
        TasksUiState.Loading -> {
            CircularProgressIndicator()
        }
        
        is TasksUiState.Success -> {
            Box(modifier = Modifier.fillMaxSize()) {
                if (editDialog) {
                    EditTaskDialog(
                        show = editDialog,
                        item = editItem,
                        viewModel = tasksViewModel
                    )
                    
                } else {
                    AddTaskDialog(
                        show = showDialog,
                        onDismiss = { tasksViewModel.onDialogClose() },
                        onTaskAdded = { tasksViewModel.onTasksCreated(it) })
                }
                FABDialog(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    tasksViewModel
                
                )
                TasksList((uiState as TasksUiState.Success).tasks, tasksViewModel)
            }
        }
    }
}

@Composable
fun TasksList(tasksList: List<TaskModel>, tasksViewModel: TasksViewModel) {
    LazyColumn() {
        items(tasksList, key = { it.id }) { task ->
            ItemTask(item = task, tasksViewModel = tasksViewModel)
        }
    }
}

@Composable
fun ItemTask(item: TaskModel, tasksViewModel: TasksViewModel) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    tasksViewModel.onEditMode(item)
                    tasksViewModel.onEditDialogOpen()
//                    tasksViewModel.onItemRemove(item)
                })
            },
        border = BorderStroke(0.5.dp, Color.LightGray)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.selected,
                onCheckedChange = { tasksViewModel.onCheckBoxSelected(item) })
            Text(
                text = item.taskName, modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            )
        }
    }
    
}

@Composable
fun FABDialog(modifier: Modifier, tasksViewModel: TasksViewModel) {
    FloatingActionButton(
        onClick = { tasksViewModel.onShowDialogClick() },
        modifier = modifier
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Task")
    }
}

@Composable
fun EditTaskDialog(
    show: Boolean,
    item: TaskModel,
    viewModel: TasksViewModel
) {
    var myTaskStatus by rememberSaveable { mutableStateOf(item.taskName) }
    if (show) {
        Dialog(onDismissRequest = { viewModel.onEditDialogClose() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
                    .testTag("addTaskDialog"),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Editar una tarea",
                    fontFamily = FontFamily.Cursive,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                TextField(
                    value = myTaskStatus,
                    onValueChange = { myTaskStatus = it },
                    maxLines = 3,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Button(
                    onClick = {
                        viewModel.onItemUpdate( item, myTaskStatus)
                        myTaskStatus = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                ) {
                    Text(text = "Confirm edit")
                }
                
            }
            
        }
    }
}

@Composable
fun AddTaskDialog(show: Boolean, onDismiss: () -> Unit, onTaskAdded: (String) -> Unit) {
    var myTaskStatus by rememberSaveable { mutableStateOf("") }
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
                    .testTag("addTaskDialog"),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Añadir una tarea",
                    fontFamily = FontFamily.Cursive,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                TextField(
                    value = myTaskStatus,
                    onValueChange = { myTaskStatus = it },
                    maxLines = 3,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Button(
                    onClick = {
                        onTaskAdded(myTaskStatus)
                        myTaskStatus = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                ) {
                    Text(text = "Add Task")
                }
                
            }
            
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun dialogPreview() {
    AddTaskDialog(show = true, onDismiss = {}, onTaskAdded = {})
}
