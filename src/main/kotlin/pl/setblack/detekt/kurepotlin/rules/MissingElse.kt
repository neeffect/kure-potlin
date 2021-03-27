package pl.setblack.detekt.kurepotlin.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtIfExpression

class MissingElse(config: Config = Config.empty) : Rule(config) {

    override val active: Boolean
        get() = valueOrDefault(Config.ACTIVE_KEY, false)

    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "if should be an expression with else block",
        Debt.FIVE_MINS
    )

    override fun visitIfExpression(expression: KtIfExpression) {
        val file = expression.containingKtFile
        val anElse = expression.`else`
        if (anElse == null) {
            report(
                CodeSmell(
                    issue, Entity.from(expression),
                    message = "if statement in the file ${file.name} has no else."
                )
            )
        }
        super.visitIfExpression(expression)
    }
}
