package pl.setblack.detekt.kurepotlin.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtLoopExpression

class LoopDefinition(config: Config = Config.empty) : Rule(config) {

    override val active: Boolean
        get() = valueOrDefault(Config.ACTIVE_KEY, true)

    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "Shouldn't use loops in functional code",
        Debt.TWENTY_MINS
    )

    override fun visitLoopExpression(loopExpression: KtLoopExpression) {
        val file = loopExpression.containingKtFile
        report(
            CodeSmell(
                issue, Entity.from(loopExpression),
                message = "The file ${file.name} contains loop."
            )
        )
        super.visitLoopExpression(loopExpression)
    }
}
