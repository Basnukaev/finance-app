package com.basnukaev.finance.app.telegram.provider

abstract class Container<T>(
    private val value: T?,
) {

    fun get(): T {
        return value ?: throw NullPointerException("You injected some object into a place where it could not appear")
    }
}