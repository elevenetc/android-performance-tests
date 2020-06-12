package com.elevenetc.android.flat.performance

import android.content.Context
import android.os.Environment
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.BufferedReader
import java.io.File
import java.io.FileReader


val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

var jsonAdapter: JsonAdapter<PerfLog> = moshi.adapter(PerfLog::class.java)

fun writeTimeFile(className: String, methodName: String, avg: Double, context: Context) {


    val state = Environment.getExternalStorageState()
    val baseDir = if (Environment.MEDIA_MOUNTED == state) {
        val baseDirFile: File? = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        if (baseDirFile == null) {
            throw RuntimeException("no downloads dir")
        } else {
            baseDirFile.absolutePath
        }
    } else {
        throw RuntimeException("no mounted disk")
    }

    val logDir = File(baseDir, "pef-dir")
    if (!logDir.exists()) logDir.mkdir()
    val logFile = File(logDir, "perf.json")
    if (!logFile.exists()) {
        logFile.createNewFile()

        val log = PerfLog()
        val perfClass = PerfLog.PerfClass()
        perfClass.methods[methodName] = avg
        log.classes[className] = perfClass

        logFile.writeText(jsonAdapter.toJson(log))
    } else {
        val file = loadFile(logFile)
        val log = jsonAdapter.fromJson(file)!!



        if (log.classes.containsKey(className)) {
            val clazz = log.classes[className]!!
            clazz.methods[methodName] = avg
        } else {
            val perfClass = PerfLog.PerfClass()
            perfClass.methods[methodName] = avg
            log.classes[className] = perfClass
        }

        logFile.writeText(jsonAdapter.toJson(log))
    }
}

fun loadFile(file: File): String {

    val text = StringBuilder()

    val br = BufferedReader(FileReader(file));
    val line = ""

    BufferedReader(br).use { r ->
        r.lineSequence().forEach {
            text.append(it)
            text.append('\n')
        }
    }
    br.close();

    return text.toString()
}

data class PerfLog(val classes: MutableMap<String, PerfClass> = mutableMapOf()) {
    data class PerfClass(val methods: MutableMap<String, Double> = mutableMapOf())
}