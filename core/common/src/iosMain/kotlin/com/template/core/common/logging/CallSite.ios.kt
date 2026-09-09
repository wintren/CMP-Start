package com.template.core.common.logging

/**
 * Native has to symbolicate, which is expensive enough that a log in a loop becomes the loop's
 * cost. Measured, not assumed.
 */
internal actual fun callSite(): String? = null
