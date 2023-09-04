package com.elthobhy.islamicstory.core.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.elthobhy.islamicstory.core.databinding.DialogAnimationLayoutBinding
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

fun uriToFile(selectedImage: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val file = createTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImage) as InputStream
    val outputStream: OutputStream = FileOutputStream(file)
    val buf = ByteArray(1024)
    var length: Int
    while (inputStream.read(buf).also { length = it } > 0) outputStream.write(buf, 0, length)
    outputStream.close()
    inputStream.close()

    return file
}

fun createTempFile(context: Context): File {
    val directory: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", directory)
}

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun showDialogAnimation(context: Context, state: String, message: String? = null, animation: String): AlertDialog{
    val dialogView = DialogAnimationLayoutBinding.inflate(LayoutInflater.from(context))
    dialogView.animationLottie.setAnimation(animation)
    dialogView.tvMessage.text = message 
    dialogView.tvEmpty.text = state
    val alert = AlertDialog
        .Builder(context)
        .setView(dialogView.root)
        .setCancelable(true)
        .create()
    alert.window?.decorView?.setBackgroundResource(android.R.color.transparent)
    return alert
}

fun dialogLoading(context: Context) = showDialogAnimation(
    context = context,
    state = "Please Wait",
    animation = "status_loading.json"
)
fun dialogError(e: String?, context: Context) =
    showDialogAnimation(
        context = context,
        message = e,
        state = "Error",
        animation = "bedug.json")
fun dialogSuccess(context: Context) =
    showDialogAnimation(context = context, state = "Success", animation = "ketupat.json")