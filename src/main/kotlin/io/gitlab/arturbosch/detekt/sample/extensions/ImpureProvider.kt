package io.gitlab.arturbosch.detekt.sample.extensions

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import io.gitlab.arturbosch.detekt.sample.extensions.rules.ImpureCode
import io.gitlab.arturbosch.detekt.sample.extensions.rules.ObjectOrientedClassCode

class ImpureProvider : RuleSetProvider {

    override val ruleSetId: String = "impure"

    override fun instance(config: Config): RuleSet = RuleSet(
        ruleSetId,
        listOf(
            ImpureCode(),
            ObjectOrientedClassCode(config)
        )
    )
}
