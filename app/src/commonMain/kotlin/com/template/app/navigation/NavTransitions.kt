package com.template.app.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.navigation3.scene.Scene

private const val DURATION_MS = 260
private const val ENTER_SCALE = 0.94f
private const val EXIT_SCALE = 1.06f

fun <T : Any> AnimatedContentTransitionScope<Scene<T>>.forwardTransition(): ContentTransform =
    (scaleIn(tween(DURATION_MS), initialScale = ENTER_SCALE) + fadeIn(tween(DURATION_MS))) togetherWith
        ExitTransition.None

fun <T : Any> AnimatedContentTransitionScope<Scene<T>>.backwardTransition(): ContentTransform =
    EnterTransition.None togetherWith
        (scaleOut(tween(DURATION_MS), targetScale = EXIT_SCALE) + fadeOut(tween(DURATION_MS)))
