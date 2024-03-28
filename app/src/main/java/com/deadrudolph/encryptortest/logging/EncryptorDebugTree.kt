package com.deadrudolph.encryptortest.logging

import androidx.annotation.Keep
import timber.log.Timber

@Keep
class EncryptorDebugTree : Timber.DebugTree() {

    /**
     * It takes a stack trace element and returns a string that is a combination of the file name, line
     * number, and method name
     *
     * @param element The stack trace element.
     * @return The string "(${element.fileName}:${element.lineNumber})#${element.methodName}"
     */
    override fun createStackElementTag(element: StackTraceElement): String {
        return String.format(
            "(%s:%s)#%s",
            element.fileName,
            element.lineNumber,
            element.methodName
        )
    }
}
