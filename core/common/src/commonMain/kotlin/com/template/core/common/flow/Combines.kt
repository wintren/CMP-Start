@file:Suppress("UNCHECKED_CAST")

package com.template.core.common.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

/**
 * Destructurable tuples instead of `Array<Any?>`. Always prefer this over `combine`, whose
 * array overload erases every type past the first.
 */
fun <T1> combines(flow1: Flow<T1>): Flow<Tuple1<T1>> =
    flow1.map(::Tuple1)

fun <T1, T2> combines(flow1: Flow<T1>, flow2: Flow<T2>): Flow<Tuple2<T1, T2>> =
    combine(flow1, flow2, ::Pair)

fun <T1, T2, T3> combines(
    flow1: Flow<T1>, flow2: Flow<T2>, flow3: Flow<T3>,
): Flow<Tuple3<T1, T2, T3>> = combine(flow1, flow2, flow3, ::Triple)

fun <T1, T2, T3, T4> combines(
    flow1: Flow<T1>, flow2: Flow<T2>, flow3: Flow<T3>, flow4: Flow<T4>,
): Flow<Tuple4<T1, T2, T3, T4>> = combine(flow1, flow2, flow3, flow4, ::Tuple4)

fun <T1, T2, T3, T4, T5> combines(
    flow1: Flow<T1>, flow2: Flow<T2>, flow3: Flow<T3>, flow4: Flow<T4>, flow5: Flow<T5>,
): Flow<Tuple5<T1, T2, T3, T4, T5>> = combine(flow1, flow2, flow3, flow4, flow5, ::Tuple5)

fun <T1, T2, T3, T4, T5, T6> combines(
    flow1: Flow<T1>, flow2: Flow<T2>, flow3: Flow<T3>, flow4: Flow<T4>, flow5: Flow<T5>,
    flow6: Flow<T6>,
): Flow<Tuple6<T1, T2, T3, T4, T5, T6>> =
    combine(flow1, flow2, flow3, flow4, flow5, flow6) {
        Tuple6(it[0] as T1, it[1] as T2, it[2] as T3, it[3] as T4, it[4] as T5, it[5] as T6)
    }

fun <T1, T2, T3, T4, T5, T6, T7> combines(
    flow1: Flow<T1>, flow2: Flow<T2>, flow3: Flow<T3>, flow4: Flow<T4>, flow5: Flow<T5>,
    flow6: Flow<T6>, flow7: Flow<T7>,
): Flow<Tuple7<T1, T2, T3, T4, T5, T6, T7>> =
    combine(flow1, flow2, flow3, flow4, flow5, flow6, flow7) {
        Tuple7(
            it[0] as T1, it[1] as T2, it[2] as T3, it[3] as T4, it[4] as T5, it[5] as T6,
            it[6] as T7,
        )
    }

fun <T1, T2, T3, T4, T5, T6, T7, T8> combines(
    flow1: Flow<T1>, flow2: Flow<T2>, flow3: Flow<T3>, flow4: Flow<T4>, flow5: Flow<T5>,
    flow6: Flow<T6>, flow7: Flow<T7>, flow8: Flow<T8>,
): Flow<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> =
    combine(flow1, flow2, flow3, flow4, flow5, flow6, flow7, flow8) {
        Tuple8(
            it[0] as T1, it[1] as T2, it[2] as T3, it[3] as T4, it[4] as T5, it[5] as T6,
            it[6] as T7, it[7] as T8,
        )
    }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9> combines(
    flow1: Flow<T1>, flow2: Flow<T2>, flow3: Flow<T3>, flow4: Flow<T4>, flow5: Flow<T5>,
    flow6: Flow<T6>, flow7: Flow<T7>, flow8: Flow<T8>, flow9: Flow<T9>,
): Flow<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> =
    combine(flow1, flow2, flow3, flow4, flow5, flow6, flow7, flow8, flow9) {
        Tuple9(
            it[0] as T1, it[1] as T2, it[2] as T3, it[3] as T4, it[4] as T5, it[5] as T6,
            it[6] as T7, it[7] as T8, it[8] as T9,
        )
    }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> combines(
    flow1: Flow<T1>, flow2: Flow<T2>, flow3: Flow<T3>, flow4: Flow<T4>, flow5: Flow<T5>,
    flow6: Flow<T6>, flow7: Flow<T7>, flow8: Flow<T8>, flow9: Flow<T9>, flow10: Flow<T10>,
): Flow<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> =
    combine(flow1, flow2, flow3, flow4, flow5, flow6, flow7, flow8, flow9, flow10) {
        Tuple10(
            it[0] as T1, it[1] as T2, it[2] as T3, it[3] as T4, it[4] as T5, it[5] as T6,
            it[6] as T7, it[7] as T8, it[8] as T9, it[9] as T10,
        )
    }
