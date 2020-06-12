package com.elevenetc.android.flat.performance

import android.app.Activity
import android.os.Build
import android.os.Environment
import androidx.test.jank.IMonitor
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


open class ActivityPerfTestRule<T : Activity>(activityClass: Class<T>) : ActivityTestRule<T>(activityClass) {

    lateinit var monitor: IMonitor
    lateinit var annotation: PerformanceTest

    var className = ""
    var methodName = ""

    init {
        if (API_LEVEL_ACTUAL <= 22) error("Not supported by current platform.")
    }

    override fun apply(base: Statement, description: Description): Statement {
        if (description.getAnnotation(PerformanceTest::class.java) != null) {
            annotation = description.getAnnotation(PerformanceTest::class.java)!!
            monitor = PerfMonitor(getInstrumentation(), annotation.processName)
            className = description.className
            methodName = description.displayName
        }

        return super.apply(base, description)
    }

    override fun beforeActivityLaunched() {
        monitor.startIteration()
        super.beforeActivityLaunched()
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


        val averageFrameTime = annotation.averageFrameTime

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
        val context = getInstrumentation().targetContext



        writeTimeFile(className, methodName, result, context)
    }

    fun givePerm() {

        val context = getInstrumentation().context
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