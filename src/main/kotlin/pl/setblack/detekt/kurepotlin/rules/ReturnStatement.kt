package pl.setblack.detekt.kurepotlin.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtReturnExpression

class ReturnStatement(config: Config = Config.empty) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "Pure function shouldn't have return statement",
        Debt.TWENTY_MINS
    )

    override fun visitReturnExpression(expression: KtReturnExpression) {
        val file = expression.containingKtFile
        report(
            CodeSmell(
                issue, Entity.from(expression),
                message = "The file ${file.name} contains `return` statement."
            )
        )
        super.visitReturnExpression(expression)
    }

}
