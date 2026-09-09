package com.template.core.common.flow

typealias Tuple2<A, B> = Pair<A, B>
typealias Tuple3<A, B, C> = Triple<A, B, C>

data class Tuple1<A>(val first: A)

data class Tuple4<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

data class Tuple5<A, B, C, D, E>(
    val first: A, val second: B, val third: C, val fourth: D, val fifth: E,
)

data class Tuple6<A, B, C, D, E, F>(
    val first: A, val second: B, val third: C, val fourth: D, val fifth: E, val sixth: F,
)

data class Tuple7<A, B, C, D, E, F, G>(
    val first: A, val second: B, val third: C, val fourth: D, val fifth: E, val sixth: F,
    val seventh: G,
)

data class Tuple8<A, B, C, D, E, F, G, H>(
    val first: A, val second: B, val third: C, val fourth: D, val fifth: E, val sixth: F,
    val seventh: G, val eighth: H,
)

data class Tuple9<A, B, C, D, E, F, G, H, I>(
    val first: A, val second: B, val third: C, val fourth: D, val fifth: E, val sixth: F,
    val seventh: G, val eighth: H, val ninth: I,
)

data class Tuple10<A, B, C, D, E, F, G, H, I, J>(
    val first: A, val second: B, val third: C, val fourth: D, val fifth: E, val sixth: F,
    val seventh: G, val eighth: H, val ninth: I, val tenth: J,
)
