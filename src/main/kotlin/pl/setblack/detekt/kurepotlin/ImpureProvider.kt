package pl.setblack.detekt.kurepotlin

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import pl.setblack.detekt.kurepotlin.rules.AbstractClassDefinition
import pl.setblack.detekt.kurepotlin.rules.ClassDefinition
import pl.setblack.detekt.kurepotlin.rules.LoopDefinition
import pl.setblack.detekt.kurepotlin.rules.MutableCollections
import pl.setblack.detekt.kurepotlin.rules.ReturnStatement
import pl.setblack.detekt.kurepotlin.rules.ReturnUnit
import pl.setblack.detekt.kurepotlin.rules.ThrowExpression
import pl.setblack.detekt.kurepotlin.rules.VariableDefinition


class ImpureProvider : RuleSetProvider {

    override val ruleSetId: String = "impure"

    override fun instance(config: Config): RuleSet = RuleSet(
        ruleSetId,
        listOf(
            LoopDefinition(config),
            ReturnStatement(config),
            VariableDefinition(config),
            ReturnUnit(config),
            ClassDefinition(config),
            AbstractClassDefinition(config),
            ThrowExpression(config),
            MutableCollections(config)
        )
    )
}
