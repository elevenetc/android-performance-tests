package com.elevenetc.android.flat.performance

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Environment
import androidx.test.jank.IMonitor
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.File


open class ActivityPerfTestRule<T : Activity>(activityClass: Class<T>) : ActivityTestRule<T>(activityClass) {

    lateinit var monitor: IMonitor
    lateinit var annotation: PerformanceTest

    init {
        if (API_LEVEL_ACTUAL <= 22) error("Not supported by current platform.")
    }

    override fun apply(base: Statement, description: Description): Statement {
        if (description.getAnnotation(PerformanceTest::class.java) != null) {
            annotation = description.getAnnotation(PerformanceTest::class.java)!!
            monitor = PerfMonitor(InstrumentationRegistry.getInstrumentation(), annotation.processName)
        }

        return super.apply(base, description)
    }

    override fun beforeActivityLaunched() {
        monitor.startIteration()
        super.beforeActivityLaunched()
    }

    override fun launchActivity(startIntent: Intent?): T {
        return super.launchActivity(startIntent)
    }

    override fun afterActivityLaunched() {
        super.afterActivityLaunched()
    }


    override fun afterActivityFinished() {
        val results = monitor.stopIteration()
        val type = annotation.perfType.type

        val get = results.get(type)
        var result = 0.0

        if (get is Double) {
            result = get
        } else if (get is Int) {
            result = get.toDouble()
        }

        //val res: Double = results.getDouble(type, results.getInt(type).toDouble())

        val averageFrameTime = annotation.averageFrameTime

        if (averageFrameTime == 0f) {

        }

        val assertion = when (annotation.assertionType) {
            PerformanceTest.AssertionType.LESS -> result < averageFrameTime
            PerformanceTest.AssertionType.LESS_OR_EQUAL -> result <= averageFrameTime
            PerformanceTest.AssertionType.GREATER -> result > averageFrameTime
            PerformanceTest.AssertionType.GREATER_OR_EQUAL -> result >= averageFrameTime
            PerformanceTest.AssertionType.EQUAL -> result == averageFrameTime.toDouble()
            null -> false
        }
//        TestCase.assertTrue(
//            String.format(
//                "Monitor: %s, Expected: %f, Received: %f.",
//                type, averageFrameTime, result
//            ),
//            assertion
//        )

        val context = InstrumentationRegistry.getInstrumentation().context


        //activity.getExternalFilesDir()
        val filesDir = context.filesDir
        val ss = context.fileList()

        givePerm()
        xxx()

        val externalStorageState = Environment.getExternalStorageState()

        val externalFilesDir = context.getExternalFilesDir(null)
        val externalFilesDirs = context.getExternalFilesDirs(null)

        externalFilesDirs.forEach {
            println(it)
        }

        if (externalFilesDir != null && externalFilesDirs != null) {
            val destPath: String = externalFilesDir.absolutePath
        }


        val dir = File(filesDir, "test-result")
        val file = File(dir, "hello.txt")
        if (!dir.exists()) {
            dir.mkdir()
            file.writeText("zed")
        }



        super.afterActivityFinished()
    }

    fun xxx() {

        val context = InstrumentationRegistry.getInstrumentation().context
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
                file.writeText("s")
            }
        }
    }

    fun givePerm() {

        val context = InstrumentationRegistry.getInstrumentation().context
        val packageName = context.packageName

        getInstrumentation().uiAutomation.executeShellCommand("pm grant $packageName android.permission.WRITE_EXTERNAL_STORAGE")

        if (packageName.isEmpty()) {

        }
    }

    fun zed() {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

    }

    companion object {
        internal val API_LEVEL_ACTUAL =
            Build.VERSION.SDK_INT + if ("REL" == Build.VERSION.CODENAME) 0 else 1
    }
}