package com.elevenetc.android.flat.performance

import org.junit.Test
import java.io.PrintWriter

class TestK {

    @Test
    fun xxx() {

        val currentThread = Thread.currentThread()

        if (currentThread == null) {

        }

        val writer = PrintWriter("the-file-name.txt", "UTF-8")
        writer.println("The first line")
        writer.println("The second line")
        writer.close()
    }

}