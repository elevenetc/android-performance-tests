package com.elevenetc.android.flat.performance

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.test.jank.IMonitor
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import junit.framework.TestCase
import org.junit.runner.Description
import org.junit.runners.model.Statement

open class ActivityPerfTestRule<T : Activity>(activityClass: Class<T>) : ActivityTestRule<T>(activityClass) {

    lateinit var monitor: IMonitor
    lateinit var annotation: PerformanceTest

    init {
        if (API_LEVEL_ACTUAL <= 22) error("Not supported by current platform.")
    }

    override fun apply(base: Statement, description: Description): Statement {
        annotation = description.getAnnotation(PerformanceTest::class.java)!!
        monitor = PerfMonitor(InstrumentationRegistry.getInstrumentation(), annotation.processName)
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
        var result:Double = 0.0

        if (get is Double) {
            result = get
        } else if (get is Int) {
            result = get.toDouble()
        }

        //val res: Double = results.getDouble(type, results.getInt(type).toDouble())

        val assertion = when (annotation.assertionType) {
            PerformanceTest.AssertionType.LESS -> result < annotation.averageFrameTimeMs
            PerformanceTest.AssertionType.LESS_OR_EQUAL -> result <= annotation!!.averageFrameTimeMs
            PerformanceTest.AssertionType.GREATER -> result > annotation!!.averageFrameTimeMs
            PerformanceTest.AssertionType.GREATER_OR_EQUAL -> result >= annotation!!.averageFrameTimeMs
            PerformanceTest.AssertionType.EQUAL -> result == annotation!!.averageFrameTimeMs.toDouble()
            null -> false
        }
        TestCase.assertTrue(
            String.format(
                "Monitor: %s, Expected: %f, Received: %f.",
                type, annotation.averageFrameTimeMs, result
            ),
            assertion
        )
        super.afterActivityFinished()
    }

    companion object {
        internal val API_LEVEL_ACTUAL =
            Build.VERSION.SDK_INT + if ("REL" == Build.VERSION.CODENAME) 0 else 1
    }
}