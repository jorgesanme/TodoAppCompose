package com.jorgesm.todoappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jorgesm.todoappcompose.navigation.TasksScreen
import com.jorgesm.todoappcompose.features.addtasks.ui.TasksViewModel
import com.jorgesm.todoappcompose.features.addtasks.ui.models.Routes
import com.jorgesm.todoappcompose.navigation.PhotoTaker
import com.jorgesm.todoappcompose.ui.theme.TodoAppComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val tasksViewModel: TasksViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoAppComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navigationController = rememberNavController()
                    NavHost(navController = navigationController, startDestination = Routes.TaskScreen.route) {
                        composable(Routes.TaskScreen.route) { TasksScreen(tasksViewModel = tasksViewModel, navigationController) }
                        composable(Routes.PhotoTaker.route) { backStackEntry ->
                            val item =  backStackEntry.arguments?.getString("item").orEmpty()
                            PhotoTaker( viewModel = tasksViewModel, navigationController) }
                    }
                }
            }
        }
    }
}