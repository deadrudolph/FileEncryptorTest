package com.deadrudolph.encryptortest.encryptor.utils

import kotlin.reflect.KClass

inline fun <T> (() -> T?).multiCatch(
    vararg exceptions: KClass<out Throwable>,
    onError: (message: String?) -> T
): T? {
    return try {
        this()
    } catch (exception: Exception) {
        if (exception::class in exceptions) onError(exception.localizedMessage) else throw exception
    }
}
