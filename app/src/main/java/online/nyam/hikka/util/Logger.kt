package online.nyam.hikka.util

import android.content.res.Resources
import android.util.Log
import online.nyam.hikka.R

private val TAG = Resources.getSystem().getString(R.string.app_name)

@Suppress("unused")
class Logger(
    private val tag: String = ""
) {
    fun sub(tag: String) = Logger("${this.tag}.$tag")

    private fun constructMessage(message: String): String {
        if (tag.isNotEmpty()) {
            return "[$tag] $message"
        }

        return message
    }

    private fun log(
        level: Int,
        message: String
    ) {
        if (Log.isLoggable(TAG, level)) {
            Log.println(level, TAG, constructMessage(message))
        }
    }

    private fun log(
        level: Int,
        message: () -> String
    ) {
        if (Log.isLoggable(TAG, level)) {
            Log.println(level, TAG, constructMessage(message()))
        }
    }

    fun v(message: String) = log(Log.VERBOSE, message)

    fun v(message: () -> String) = log(Log.VERBOSE, message)

    fun d(message: String) = log(Log.DEBUG, message)

    fun d(message: () -> String) = log(Log.DEBUG, message)

    fun i(message: String) = log(Log.INFO, message)

    fun i(message: () -> String) = log(Log.INFO, message)

    fun w(message: String) = log(Log.WARN, message)

    fun w(message: () -> String) = log(Log.WARN, message)

    fun e(message: String) = log(Log.ERROR, message)

    fun e(message: () -> String) = log(Log.ERROR, message)
}
