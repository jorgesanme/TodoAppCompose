package com.jorgesm.todoappcompose.navigation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.jorgesm.todoappcompose.BitmapConverter
import com.jorgesm.todoappcompose.createImageFile
import com.jorgesm.todoappcompose.features.addtasks.ui.TasksViewModel
import com.jorgesm.todoappcompose.features.addtasks.ui.component.MyImage
import com.jorgesm.todoappcompose.features.addtasks.ui.models.Routes
import com.jorgesm.todoappcompose.features.addtasks.ui.models.TaskModel
import com.jorgesm.todoappcompose.ui.theme.FABColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Objects


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoTaker(viewModel: TasksViewModel, navigationController: NavHostController) {
    val item: TaskModel by viewModel.lastItemSelected.collectAsStateWithLifecycle()
    val photoUriState: Uri by viewModel.photoUriState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            viewModel.onUriUpdate(uri)
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navigationController.navigate(Routes.TaskScreen.route) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "volver")
                    }
                },
                title = { Text(text = "Hacer fotos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FABColor
                )

            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var imgString = ""
            if (item.imageString.isNotEmpty()) {
                imgString = item.imageString
            } else {
                val imgBitmap = BitmapConverter.uriToBitmap(photoUriState, context)
                imgBitmap?.let { imgString = BitmapConverter.convertBitmapToString(it) }
            }

            MyImage(
                imgUri = imgString,
                modifier = Modifier
                    .size(300.dp)
                    .padding(8.dp)
            )
            Row() {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    onClick = {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch(uri)
                        } else {
                            // Request a permission
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }) {
                    Text(text = "Camera")
                }
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    onClick = {
                        navigationController.navigate(Routes.TaskScreen.route)
                        viewModel.run {
                            viewModelScope.launch(Dispatchers.IO) {
                                onPhotoUpdate(item, "")
                                onUriUpdate(Uri.EMPTY)
                            }
                        }
                    }
                ) {
                    Text(text = "Remove")
                }
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    onClick = {
                        val bitmap = BitmapConverter.uriToBitmap(photoUriState, context)
                        var imgString: String = ""
                        bitmap.let {
                            it?.let {
                                imgString = BitmapConverter.convertBitmapToString(it)
                            }
                        }

                        viewModel.run {
                            this.viewModelScope.launch(Dispatchers.IO) {
                                onPhotoUpdate(item, imgString)
                            }
                        }
                        navigationController.navigate(Routes.TaskScreen.route)
                    }) {
                    Text(text = "Save")
                }
            }
        }
    }
}

