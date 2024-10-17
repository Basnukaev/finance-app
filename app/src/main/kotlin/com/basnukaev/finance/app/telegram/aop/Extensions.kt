package com.basnukaev.finance.app.telegram.aop


inline fun <reified T> Array<*>.getByTyp(): T {
    for (arg in this) {
        if (arg is T) {
            return arg
        }
    }
    throw IllegalArgumentException("No argument of type ${T::class.simpleName} found")
}