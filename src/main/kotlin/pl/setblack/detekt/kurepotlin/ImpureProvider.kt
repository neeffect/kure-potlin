package pl.setblack.detekt.kurepotlin

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import pl.setblack.detekt.kurepotlin.rules.ImpureCode
import pl.setblack.detekt.kurepotlin.rules.ObjectOrientedClassCode
import pl.setblack.detekt.kurepotlin.rules.ThrowExpression
import pl.setblack.detekt.kurepotlin.rules.MutableCollectionsCode


class ImpureProvider : RuleSetProvider {

    override val ruleSetId: String = "impure"

    override fun instance(config: Config): RuleSet = RuleSet(
        ruleSetId,
        listOf(
            ImpureCode(),
            ObjectOrientedClassCode(config),
            ThrowExpression(config),
            MutableCollectionsCode(config)

        )
    )
}
