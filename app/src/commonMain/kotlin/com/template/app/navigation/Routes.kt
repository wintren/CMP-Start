package com.template.app.navigation

/**
 * One codec for three jobs: the browser's address bar, an Android deep link, and the back stack
 * saved across launches. A new destination is added here once and all three follow.
 *
 * [destinationOf] returning null is the normal answer for a link we do not recognise — a URL is
 * user input, and an unknown one means "start where you always start", never a crash.
 */
fun Destination.toRoute(): String = when (this) {
    Destination.Locations -> LOCATIONS
    Destination.BestDay -> BEST_DAY
    Destination.Settings -> SETTINGS
    is Destination.Forecast -> "$FORECAST/$locationId"
}

fun destinationOf(route: String): Destination? {
    val segments = route.trim('/').split('/')
    return when (segments.firstOrNull()) {
        LOCATIONS -> Destination.Locations
        BEST_DAY -> Destination.BestDay
        SETTINGS -> Destination.Settings
        FORECAST -> segments.getOrNull(1)?.toLongOrNull()?.let(Destination::Forecast)
        else -> null
    }
}

/**
 * The stack a single destination needs beneath it. A link straight to a forecast still has
 * somewhere to go back to, and on a wide window it opens beside its list for free.
 */
fun stackFor(destination: Destination): List<Destination> = when (destination) {
    is Destination.Forecast -> listOf(Destination.Locations, destination)
    else -> listOf(destination)
}

/** The whole stack as one line, for storage. A URL carries only the destination on top. */
fun List<Destination>.toRoutePath(): String = joinToString(STACK_SEPARATOR) { it.toRoute() }

fun backStackOf(path: String): List<Destination> =
    path.split(STACK_SEPARATOR).mapNotNull(::destinationOf)

private const val LOCATIONS = "locations"
private const val BEST_DAY = "best-day"
private const val SETTINGS = "settings"
private const val FORECAST = "forecast"
private const val STACK_SEPARATOR = ">"
