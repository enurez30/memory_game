package com.sera.memorygame.extentions

import kotlinx.coroutines.flow.Flow

inline fun <reified T1, reified T2, reified T3, reified T4, reified T5, reified T6, reified T7, reified T8, reified T9, R> combine(
    f1: Flow<T1>,
    f2: Flow<T2>,
    f3: Flow<T3>,
    f4: Flow<T4>,
    f5: Flow<T5>,
    f6: Flow<T6>,
    f7: Flow<T7>,
    f8: Flow<T8>,
    f9: Flow<T9>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9) -> R
) = kotlinx.coroutines.flow.combine(listOf(f1, f2, f3, f4, f5, f6, f7, f8, f9)) {
    transform(
        it[0] as T1,
        it[1] as T2,
        it[2] as T3,
        it[3] as T4,
        it[4] as T5,
        it[5] as T6,
        it[6] as T7,
        it[7] as T8,
        it[8] as T9
    )
}

inline fun <reified T1, reified T2, reified T3, reified T4, reified T5, reified T6, reified T7, reified T8, reified T9, reified T10, R> combine(
    f1: Flow<T1>,
    f2: Flow<T2>,
    f3: Flow<T3>,
    f4: Flow<T4>,
    f5: Flow<T5>,
    f6: Flow<T6>,
    f7: Flow<T7>,
    f8: Flow<T8>,
    f9: Flow<T9>,
    f10: Flow<T10>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) -> R
) = kotlinx.coroutines.flow.combine(listOf(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10)) {
    transform(
        it[0] as T1,
        it[1] as T2,
        it[2] as T3,
        it[3] as T4,
        it[4] as T5,
        it[5] as T6,
        it[6] as T7,
        it[7] as T8,
        it[8] as T9,
        it[9] as T10
    )
}

inline fun <reified T1, reified T2, reified T3, reified T4, reified T5, reified T6, R> combine(
        f1: Flow<T1>,
        f2: Flow<T2>,
        f3: Flow<T3>,
        f4: Flow<T4>,
        f5: Flow<T5>,
        f6: Flow<T6>,
        crossinline transform: suspend (T1, T2, T3, T4, T5, T6) -> R
) = kotlinx.coroutines.flow.combine(listOf(f1, f2, f3, f4, f5, f6)) {
    transform(
            it[0] as T1,
            it[1] as T2,
            it[2] as T3,
            it[3] as T4,
            it[4] as T5,
            it[5] as T6
    )
}