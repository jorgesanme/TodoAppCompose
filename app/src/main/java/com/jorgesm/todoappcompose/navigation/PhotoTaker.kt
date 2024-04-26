package com.jorgesm.todoappcompose.navigation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.jorgesm.todoappcompose.BitmapConverter
import com.jorgesm.todoappcompose.features.addtasks.ui.TasksViewModel
import com.jorgesm.todoappcompose.features.addtasks.ui.component.MyImage
import com.jorgesm.todoappcompose.features.addtasks.ui.models.Routes
import com.jorgesm.todoappcompose.features.addtasks.ui.models.TaskModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


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

    Row(Modifier.padding(16.dp)) {
        TextButton(onClick = { navigationController.navigate(Routes.TaskScreen.route) }) {
            Text(text = "Volver", color = Color.LightGray)
        }
    }
    Column(
        verticalArrangement = Arrangement.Top,
        modifier =
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imgString = photoUriState

        MyImage(
            imgString = imgString,
            context = context,
            modifier = Modifier
                .size(250.dp)
                .padding(16.dp)
        )
        Row {
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
                    val uri = photoUriState
                    val bitmap = BitmapConverter.uriToBitmap(uri, context = context)

                    var imgString: String = ""

                    bitmap.let { it?.let { imgString = BitmapConverter.convertBitmapToString(it) } }
                    val img = imgString

                    viewModel.onPhotoUpdate(item, imgString)
                    navigationController.navigate(Routes.TaskScreen.route)

                }) {
                Text(text = "Save")
            }
        }
    }
}


@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}