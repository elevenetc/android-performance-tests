package com.elevenetc.android.flat.performance

import android.content.Context
import android.os.Environment
import java.io.File

fun writeTimeFile(context: Context) {


    var baseDir: String? = null
    val state = Environment.getExternalStorageState()
    if (Environment.MEDIA_MOUNTED == state) {
        val baseDirFile: File? = context.getExternalFilesDir(android.os.Environment.DIRECTORY_DOWNLOADS)
        if (baseDirFile == null) {
            baseDir = context.getFilesDir().getAbsolutePath()
        } else {
            baseDir = baseDirFile.absolutePath
        }
    } else {
        baseDir = context.getFilesDir().getAbsolutePath()
    }

    if (baseDir != null) {
        val dir = File(baseDir, "hello-dir")
        if (!dir.exists()) {
            dir.mkdir()
        }

        val file = File(dir, "hello.txt")

        if (!file.exists()) {
            file.createNewFile()
        }

        file.appendText(System.currentTimeMillis().toString() + "\n")
    }
}