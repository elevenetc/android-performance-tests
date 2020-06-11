package com.elevenetc.android.flat.performance

import android.os.Build
import java.io.File
import java.io.IOException
import java.lang.RuntimeException


internal abstract class Chmod {
    companion object {
        private var INSTANCE: Chmod? = null
        fun chmodPlusR(file: File?) {
            INSTANCE!!.plusR(file)
        }

        fun chmodPlusRWX(file: File?) {
            INSTANCE!!.plusRWX(file)
        }

        init {
            INSTANCE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                Java6Chmod()
            } else {
                Java5Chmod()
            }
        }
    }

    protected abstract fun plusR(file: File?)
    protected abstract fun plusRWX(file: File?)
}

private class Java6Chmod : Chmod() {
    override fun plusR(file: File?) {
        file!!.setReadable(true, false)
    }

    override fun plusRWX(file: File?) {
        file!!.setReadable(true, false)
        file.setWritable(true, false)
        file.setExecutable(true, false)
    }
}

private class Java5Chmod : Chmod() {
    override fun plusR(file: File?) {
        try {
            Runtime.getRuntime().exec(arrayOf("chmod", "644", file!!.absolutePath))
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun plusRWX(file: File?) {
        try {
            Runtime.getRuntime().exec(arrayOf("chmod", "777", file!!.absolutePath))
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}