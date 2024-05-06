package com.jorgesm.todoappcompose.ui

import androidx.compose.ui.test.assertContentDescriptionContains
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.doubleClick
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import com.jorgesm.todoappcompose.features.addtasks.ui.component.MyText
import org.junit.Rule
import org.junit.Test

class MyCustomComponentTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    
    
    @Test
    fun findMyTextComponent(){
        composeTestRule.setContent {
            MyText()
        }
        
        //FINDER
        //  # one by one
        composeTestRule.onNodeWithText("Estamos jodidos", ignoreCase = true )
        composeTestRule.onNodeWithTag("segundo")
        composeTestRule.onNodeWithContentDescription("visualIcon")
        
        
        //  # All at time
        composeTestRule.onAllNodesWithText("es")
        composeTestRule.onAllNodesWithTag("segundo")
        composeTestRule.onAllNodesWithContentDescription("visualIcon")
        
        //ACTIONS
        
        composeTestRule.onNodeWithText("Estamos jodidos").performClick()
        composeTestRule.onAllNodesWithText("Estamos jodidos" ).onFirst().performClick()
        composeTestRule.onNodeWithText("Estamos jodidos" ).performTouchInput {
            doubleClick()
            longClick()
            swipeLeft()
            swipeRight()
            //ETC
        }
        composeTestRule.onNodeWithText("Estamos jodidos" ).performScrollTo()
        composeTestRule.onNodeWithText("Estamos jodidos" ).performImeAction()
        //TextField
        composeTestRule.onNodeWithText("Estamos jodidos").performTextClearance()
        composeTestRule.onNodeWithText("Estamos jodidos").performTextInput("He añadido Nueva tarea")
        composeTestRule.onNodeWithText("Estamos jodidos").performTextReplacement("Texto modificado")
        
        //ASSERTIONS
        composeTestRule.onNodeWithText("Estamos jodidos" ).assertExists()
        composeTestRule.onNodeWithText("Estamos jodidos" ).assertDoesNotExist()
        composeTestRule.onNodeWithText("Estamos jodidos" ).assertContentDescriptionEquals("texto del contentDescription")
        composeTestRule.onNodeWithText("Estamos jodidos" ).assertContentDescriptionContains("texto del contentDescription")
        composeTestRule.onNodeWithText("Estamos jodidos" ).assertIsDisplayed()
    }
    
    @Test
    fun whenMyTextField_StartWithJorge(){
        composeTestRule.setContent {
            MyText()
        }
        composeTestRule.onNodeWithText("jorge", ignoreCase = true).assertExists()
        composeTestRule.onNodeWithTag("textFieldName").assertTextContains("jorge")
        composeTestRule.onNodeWithTag("textFieldName").assertTextEquals("jorge")
    }
    
    @Test
    fun whenNameIsAdded_theVerifyTextContainsGreeting(){
        composeTestRule.setContent {
            MyText()
        }
        val testText = "pepe"
//        composeTestRule.onNodeWithTag("textFieldName").performTextClearance()
        composeTestRule.onNodeWithTag("textFieldName").performTextReplacement("$testText")
        composeTestRule.onNodeWithTag("text").assertTextEquals("Te llamas pepe")
    }
}