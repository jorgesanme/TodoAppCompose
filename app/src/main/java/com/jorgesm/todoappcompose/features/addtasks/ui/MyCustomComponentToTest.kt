package com.jorgesm.todoappcompose.features.addtasks.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun MyText() {
    var name by rememberSaveable {mutableStateOf("jorge")}
    
    Column(Modifier.fillMaxSize()) {
        TextField(value = name, onValueChange = {name = it}, Modifier.testTag("textFieldName"))
        Text(text = "Te llamas $name", Modifier.testTag("text"))
        Image(imageVector = Icons.Default.AddCircle, contentDescription = "visualIcon")
    }
}