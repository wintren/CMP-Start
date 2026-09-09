package com.template.app.weather.format

import com.template.app.resources.Res
import com.template.app.resources.activity_cycling
import com.template.app.resources.activity_picnic
import com.template.app.resources.activity_running
import com.template.core.ui.resource.StringValue
import com.template.core.ui.resource.asValue
import com.template.domain.weather.model.ActivityProfile

fun ActivityProfile.label(): StringValue = when (this) {
    ActivityProfile.Running -> Res.string.activity_running
    ActivityProfile.Cycling -> Res.string.activity_cycling
    ActivityProfile.Picnic -> Res.string.activity_picnic
}.asValue()
