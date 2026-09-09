package com.template.design.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

data class AppShapes(
    val small: Shape = RoundedCornerShape(8.dp),
    val medium: Shape = RoundedCornerShape(14.dp),
    val large: Shape = RoundedCornerShape(22.dp),
    val pill: Shape = RoundedCornerShape(percent = 50),
)
