package pl.setblack.detekt.kurepotlin.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtIfExpression
import org.jetbrains.kotlin.psi.KtWhenExpression
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.bindingContextUtil.isUsedAsExpression
import org.jetbrains.kotlin.resolve.bindingContextUtil.isUsedAsResultOfLambda

class BranchStatement(config: Config = Config.empty) : Rule(config) {

    override val active: Boolean
        get() = valueOrDefault(Config.ACTIVE_KEY, true)

    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "Branch should be an expression",
        Debt.TEN_MINS
    )

    override fun visitWhenExpression(expression: KtWhenExpression) {
        bindingContext.takeIf { it != BindingContext.EMPTY }?.let {
            val file = expression.containingKtFile
            if (!expression.isUsedAsExpression(bindingContext)) {
                when (expression.context) {
                    is KtBlockExpression -> report(
                        CodeSmell(
                            issue, Entity.from(expression),
                            message = "when statement in the file ${file.name} is not an expression."
                        )
                    )
                }
            }
        }
        super.visitWhenExpression(expression)
    }

    override fun visitIfExpression(expression: KtIfExpression) {
        bindingContext.takeIf { it != BindingContext.EMPTY }?.let {
            val file = expression.containingKtFile
            if (!expression.isUsedAsExpression(bindingContext)) {
                when (expression.context) {
                    is KtBlockExpression -> report(
                        CodeSmell(
                            issue, Entity.from(expression),
                            message = "if statement in the file ${file.name} is not an expression."
                        )
                    )
                }
            }
        }
        super.visitIfExpression(expression)
    }
}
