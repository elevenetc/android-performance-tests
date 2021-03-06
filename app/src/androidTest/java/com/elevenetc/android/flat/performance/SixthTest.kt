package com.elevenetc.android.flat.performance

import android.Manifest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SixthTest {

    private lateinit var device: UiDevice

    @get:Rule
    var performanceTest = ActivityPerfTestRule(MainActivity::class.java)

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )


    @Before
    fun setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test(timeout = 50000)
    @PerformanceTest(
        BuildConfig.APPLICATION_ID,
        PerformanceTest.PerfType.AVG_FRAME_TIME_99TH,
        16f,
        PerformanceTest.AssertionType.LESS_OR_EQUAL
    )
    fun testA() {

        for (i in 0 until INNER_LOOP) {
            val appViews = UiScrollable(UiSelector().scrollable(true))
            appViews.setAsVerticalList()
            appViews.scrollTextIntoView("This is item ${MainActivity.ITEMS_COUNT - 1}")
            appViews.scrollTextIntoView("This is item 1")
        }


    }

    @Test(timeout = 50000)
    @PerformanceTest(
        BuildConfig.APPLICATION_ID,
        PerformanceTest.PerfType.AVG_FRAME_TIME_99TH,
        16f,
        PerformanceTest.AssertionType.LESS_OR_EQUAL
    )
    fun testB() {

        for (i in 0 until INNER_LOOP) {
            val appViews = UiScrollable(UiSelector().scrollable(true))
            appViews.setAsVerticalList()
            appViews.scrollTextIntoView("This is item ${MainActivity.ITEMS_COUNT - 1}")
            appViews.scrollTextIntoView("This is item 1")
        }


    }

    @Test
    @PerformanceTest(
        BuildConfig.APPLICATION_ID,
        PerformanceTest.PerfType.AVG_FRAME_TIME_99TH,
        16f,
        PerformanceTest.AssertionType.LESS_OR_EQUAL
    )
    fun testC() {
        val x = 10 + 11
        assert(x == 21)
    }

    @Test
    fun testD(){

    }

//    @Test
//    @PerformanceTest(
//        PACKAGE_NAME,
//        PerformanceTest.PerfType.AVG_NUM_JANKY,
//        5,
//        PerformanceTest.AssertionType.LESS_OR_EQUAL
//    )
//    fun testSixth2() {
//        for (i in 0 until INNER_LOOP) {
//            val appViews = UiScrollable(UiSelector().scrollable(true))
//            appViews.setAsVerticalList()
//            appViews.scrollTextIntoView("This is item 24")
//            appViews.scrollTextIntoView("This is item 1")
//        }
//    }

    companion object {
        private const val INNER_LOOP = 1
    }
}

