package pl.setblack.detekt.kurepotlin

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import pl.setblack.detekt.kurepotlin.rules.ClassDefinition
import pl.setblack.detekt.kurepotlin.rules.LoopUsage
import pl.setblack.detekt.kurepotlin.rules.MutableCollectionsCode
import pl.setblack.detekt.kurepotlin.rules.ReturnStatement
import pl.setblack.detekt.kurepotlin.rules.ReturnUnit
import pl.setblack.detekt.kurepotlin.rules.ThrowExpression
import pl.setblack.detekt.kurepotlin.rules.VariableUsage


class ImpureProvider : RuleSetProvider {

    override val ruleSetId: String = "impure"

    override fun instance(config: Config): RuleSet = RuleSet(
        ruleSetId,
        listOf(
            LoopUsage(config),
            ReturnStatement(config),
            VariableUsage(config),
            ReturnUnit(config),
            ClassDefinition(config),
            ThrowExpression(config),
            MutableCollectionsCode(config)
        )
    )
}
