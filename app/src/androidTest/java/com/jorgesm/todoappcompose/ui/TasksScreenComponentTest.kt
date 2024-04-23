package com.jorgesm.todoappcompose.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.jorgesm.todoappcompose.features.addtasks.ui.AddTaskDialog
import org.junit.Rule
import org.junit.Test

class TasksScreenComponentTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun `WhenAddTaskDialogGetATrue_thenShowDialog`() {
        composeTestRule.setContent {
            AddTaskDialog(show = true, onDismiss = {  }, onTaskAdded = {} )
        }
        
        composeTestRule.onNodeWithTag("addTaskDialog").assertIsDisplayed()
    }
    @Test
    fun `WhenAddTaskDialogGetATrue_thenHideDialog`() {
        composeTestRule.setContent {
            AddTaskDialog(show = false, onDismiss = {  }, onTaskAdded = {} )
        }
        
        composeTestRule.onNodeWithTag("addTaskDialog").assertDoesNotExist()
    }
  
}