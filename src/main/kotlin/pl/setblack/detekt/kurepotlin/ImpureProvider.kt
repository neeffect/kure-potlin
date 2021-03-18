package pl.setblack.detekt.kurepotlin

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import pl.setblack.detekt.kurepotlin.rules.ClassDefinition
import pl.setblack.detekt.kurepotlin.rules.ImpureCode
import pl.setblack.detekt.kurepotlin.rules.MutableCollectionsCode
import pl.setblack.detekt.kurepotlin.rules.ThrowExpression


class ImpureProvider : RuleSetProvider {

    override val ruleSetId: String = "impure"

    override fun instance(config: Config): RuleSet = RuleSet(
        ruleSetId,
        listOf(
            ImpureCode(),
            ClassDefinition(config),
            ThrowExpression(config),
            MutableCollectionsCode(config)

        )
    )
}
