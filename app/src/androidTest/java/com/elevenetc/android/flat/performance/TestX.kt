package com.elevenetc.android.flat.performance

import android.Manifest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test

class TestX {

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )


    @get:Rule
    val activityRule = XRule()
    //val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun zed() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        writeTimeFile(targetContext)
    }


}

class XRule : ActivityTestRule<MainActivity>(MainActivity::class.java) {
    override fun afterActivityLaunched() {

        writeTimeFile(activity)
    }
}