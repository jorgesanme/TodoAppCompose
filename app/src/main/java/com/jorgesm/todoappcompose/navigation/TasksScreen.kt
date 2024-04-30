package com.jorgesm.todoappcompose.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.jorgesm.todoappcompose.features.addtasks.ui.TasksUiState
import com.jorgesm.todoappcompose.features.addtasks.ui.TasksViewModel
import com.jorgesm.todoappcompose.features.addtasks.ui.component.ItemImage
import com.jorgesm.todoappcompose.features.addtasks.ui.component.SwipeToDeleteContainer
import com.jorgesm.todoappcompose.features.addtasks.ui.models.Routes
import com.jorgesm.todoappcompose.features.addtasks.ui.models.TaskModel
import com.jorgesm.todoappcompose.ui.theme.CardBackgroundColor
import com.jorgesm.todoappcompose.ui.theme.FABColor


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(tasksViewModel: TasksViewModel, navigationController: NavHostController) {
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
            Scaffold(
                floatingActionButtonPosition = FabPosition.Center,
                floatingActionButton = {
                    FABDialog(
                        modifier = Modifier
                            .padding(0.dp),
                        tasksViewModel
                    )
                },
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Lista de Tareas",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Serif,
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 64.dp, bottom = 4.dp)
                ) {
                    if (editDialog) {
                        EditTaskDialog(
                            show = editDialog,
                            item = editItem,
                            viewModel = tasksViewModel
                        )

                    } else {
                        AddTaskDialog(
                            show = showDialog,
                            onDismiss = { tasksViewModel.onAddDialogClose() },
                            onTaskAdded = { tasksViewModel.onTasksCreated(it) })
                    }

                    TasksList(
                        (uiState as TasksUiState.Success).tasks,
                        tasksViewModel,
                        navigationController
                    )

                }
            }
        }
    }
}

@Composable
fun TasksList(
    tasksList: List<TaskModel>,
    tasksViewModel: TasksViewModel,
    navigationController: NavHostController
) {
    LazyColumn() {
        items(tasksList, key = { it.id }) { task ->
            ItemTask(item = task, tasksViewModel = tasksViewModel, navigationController)
        }
    }
}

@Composable
fun ItemTask(
    item: TaskModel,
    tasksViewModel: TasksViewModel,
    navigationController: NavHostController
) {
    SwipeToDeleteContainer(item = item, onDelete = { tasksViewModel.onItemRemove(item) }) {
        Card(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onLongPress = {
                        tasksViewModel.onEditMode(item)
                        tasksViewModel.onEditDialogOpen()
                    })
                },
            colors = CardDefaults.cardColors(
                containerColor = CardBackgroundColor,

                ),
            border = BorderStroke(0.5.dp, Color.LightGray)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                ItemImage(
                    item.imageString,
                    modifier = Modifier
                        .size(70.dp)
                        .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                        .alpha(if (item.selected) 0.5f else 1f)
                        .clickable {
                            tasksViewModel.onPhotoPickerClicked(item)
                            navigationController.navigate(Routes.PhotoTaker.route)
                        }
                )

                Text(
                    text = item.taskName,
                    style = if (item.selected) {
                        TextStyle(
                            textDecoration = TextDecoration.LineThrough,
                            textAlign = TextAlign.Justify,
                            fontFamily = FontFamily.Cursive,
                            fontSize = 24.sp,
                            color = Color.LightGray
                        )
                    } else
                        TextStyle(
                            textDecoration = TextDecoration.None,
                            textAlign = TextAlign.Justify,
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            color = Color.DarkGray
                        ),

                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )

                Checkbox(
                    checked = item.selected,
                    modifier = Modifier.size(40.dp),
                    colors = CheckboxDefaults.colors(checkmarkColor = Color.Green),
                    onCheckedChange = { tasksViewModel.onCheckBoxSelected(item) })
            }
        }
    }
}

@Composable
fun FABDialog(modifier: Modifier, tasksViewModel: TasksViewModel) {
    FloatingActionButton(
        onClick = { tasksViewModel.onAddDialogClick() },
        containerColor = FABColor,
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
    var myTaskStatus by rememberSaveable { mutableStateOf("") }
    myTaskStatus = item.taskName
    if (show) {
        Dialog(onDismissRequest = { viewModel.onEditDialogClose() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(32.dp)
                    .testTag("addTaskDialog"),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Editar una tarea",
                    fontFamily = FontFamily.Cursive,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                OutlinedTextField(
                    value = myTaskStatus,
                    onValueChange = { myTaskStatus = it },
                    label = {},
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 8.dp)
                )
                Button(
                    onClick = {
                        viewModel.onItemUpdate(item, myTaskStatus)
                        myTaskStatus = item.taskName

                    },
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .align(Alignment.CenterHorizontally)
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
                    .padding(32.dp)
                    .testTag("addTaskDialog"),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Añadir una tarea",
                    fontFamily = FontFamily.Cursive,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                OutlinedTextField(
                    value = myTaskStatus,
                    onValueChange = { myTaskStatus = it },
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 8.dp)
                )
                Button(
                    onClick = {
                        onTaskAdded(myTaskStatus)
                        myTaskStatus = ""
                    },
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Add Task")
                }
            }
        }
    }
}

