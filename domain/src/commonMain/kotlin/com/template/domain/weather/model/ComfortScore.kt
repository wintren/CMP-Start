package com.template.domain.weather.model

/** 0..100, plus the reasons it isn't 100. */
data class ComfortScore(
    val value: Int,
    val penalties: List<ComfortPenalty>,
) {
    val isGood: Boolean get() = value >= GOOD_THRESHOLD

    companion object {
        const val MAX = 100
        const val GOOD_THRESHOLD = 70
    }
}
