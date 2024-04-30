package com.jorgesm.todoappcompose.features.addtasks.ui.component

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jorgesm.todoappcompose.BitmapConverter
import com.jorgesm.todoappcompose.R
import com.jorgesm.todoappcompose.convertToBitmap

@Composable
fun MyImage(imgUri: Uri, context: Context, modifier: Modifier) {

    if (imgUri != null) {
        if (imgUri.path?.equals("") == false) {
            AsyncImage(
                modifier = modifier
                    .rotate(90f)
                    .clip(RoundedCornerShape(18f)),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(BitmapConverter.uriToBitmap(imgUri, context = context))
                    .crossfade(true).build(),
                contentScale = ContentScale.Crop,
                contentDescription = "Image"
            )
        } else {
            Image(
                modifier = modifier,
                painter = painterResource(id = R.drawable.ic_camera),
                contentScale = ContentScale.Crop,
                contentDescription = "Else image"
            )
        }
    }
}

@Composable
fun ItemImage(imgString: String, modifier: Modifier) {
    if (imgString.isNotEmpty()) {
        AsyncImage(
            modifier = modifier
                .rotate(90f)
                .clip(RoundedCornerShape(25f)),
            model = ImageRequest.Builder(LocalContext.current).data(imgString.convertToBitmap())
                .crossfade(true).build(),
            contentDescription = "Task Image",
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            modifier = modifier,
            painter = painterResource(id = R.drawable.ic_photo),
            contentScale = ContentScale.Crop,
            contentDescription = "Else image"
        )
    }
}