package com.elevenetc.android.flat.performance

import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner

class CustomRunner: AndroidJUnitRunner() {
    override fun onDestroy() {
        super.onDestroy()
    }


    

}