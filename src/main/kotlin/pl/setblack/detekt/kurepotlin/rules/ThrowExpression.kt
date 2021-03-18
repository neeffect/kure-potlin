package pl.setblack.detekt.kurepotlin.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtThrowExpression

class ThrowExpression(config: Config = Config.empty) : Rule(config) {

    override val active: Boolean
        get() = valueOrDefault(Config.ACTIVE_KEY, true)

    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "Throwing side-effect exception",
        Debt.TWENTY_MINS
    )

    override fun visitThrowExpression(expression: KtThrowExpression) {
        report(
            CodeSmell(
                issue,
                Entity.from(expression),
                message = "Exception side-effect is thrown in a file ${expression.containingKtFile.name}."
            )
        )
        super.visitThrowExpression(expression)
    }
}
