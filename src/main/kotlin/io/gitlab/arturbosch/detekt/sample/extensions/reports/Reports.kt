package io.gitlab.arturbosch.detekt.sample.extensions.reports

import io.gitlab.arturbosch.detekt.api.Detektion
import io.gitlab.arturbosch.detekt.sample.extensions.processors.fqNamesKey

fun qualifiedNamesReport(detektion: Detektion): String? = detektion.getData(fqNamesKey)
    ?.takeIf { it.isNotEmpty() }
    ?.fold(StringBuilder()) { builder, fqName -> builder.append("$fqName\n") }
    ?.toString()
