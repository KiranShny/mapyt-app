package me.mapyt.app.core.shared

sealed class ResultOf<out T> {
    data class Success<out T>(val value: T) : ResultOf<T>()
    data class Failure(val throwable: Throwable) : ResultOf<Nothing>()
}

//ref: https://github.com/seanghay/result-of/blob/master/resultof/src/main/java/com/seanghay/resultof/result-of-ktx.kt

inline val <T> ResultOf<T>.isSuccessful: Boolean
    get() = this is ResultOf.Success

inline val <T> ResultOf<T>.isFailure: Boolean
    get() = this is ResultOf.Failure

inline val <T> ResultOf<T>.valueOrNull: T?
    get() = if (this is ResultOf.Success) value else null

inline val <T> ResultOf<T?>.valueOrThrow: T
    get() = if (this is ResultOf.Success) value!!
    else throw throwableOrNull ?: NullPointerException("value")

inline val <T> ResultOf<T>.throwableOrNull: Throwable?
    get() = if (this is ResultOf.Failure) throwable else null

inline val <T> ResultOf<T>.throwable: Throwable
    get() = if (this is ResultOf.Failure) throwable else NullPointerException("throwable")
