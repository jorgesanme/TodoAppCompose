package com.jorgesm.todoappcompose.features.addtasks.ui.component

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jorgesm.todoappcompose.BitmapConverter
import com.jorgesm.todoappcompose.R
import com.jorgesm.todoappcompose.convertToBitmap

@Composable
fun MyImage(imgString: Uri, context: Context, modifier: Modifier) {

    if (imgString != null) {
        if (imgString.path?.equals("") == false) {
            AsyncImage(
                modifier = modifier,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(BitmapConverter.uriToBitmap(imgString, context = context))
                    .crossfade(true).build(), contentDescription = ""
            )
        } else {
            Image(
                modifier = modifier,
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = "Else image"
            )
        }
    }
}

@Composable
fun ItemImage(imgString: String, modifier: Modifier) {
    if (imgString.isNotEmpty()) {
        AsyncImage(
            modifier = modifier,
            model = ImageRequest.Builder(LocalContext.current).data(imgString.convertToBitmap())
                .crossfade(true).build(), contentDescription = ""
        )
    } else {
        Image(
            modifier = modifier,
            painter = painterResource(id = R.drawable.ic_photo),
            contentDescription = "Else image"
        )
    }
}