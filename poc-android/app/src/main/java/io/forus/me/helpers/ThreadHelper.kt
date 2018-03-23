package io.forus.me.helpers

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

/**
 * Created by martijn.doornik on 22/03/2018.
 */
class ThreadHelper {

    companion object {
        val executor = Executors.newSingleThreadExecutor()
        fun <T>await(callable: Callable<T>): T {
            val result = executor.submit(callable)
            return result.get()
        }
    }
}