package pl.setblack.detekt.kurepotlin.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.rules.isMainFunction
import org.jetbrains.kotlin.asJava.namedUnwrappedElement
import org.jetbrains.kotlin.builtins.getReturnTypeFromFunctionType
import org.jetbrains.kotlin.psi.KtFunctionType
import org.jetbrains.kotlin.psi.KtLambdaExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.bindingContextUtil.getAbbreviatedTypeOrType
import org.jetbrains.kotlin.resolve.calls.callUtil.getType
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.isUnit

/**
 * @requiresTypeResolution
 */
class ReturnUnit(config: Config = Config.empty) : Rule(config) {

    override val active: Boolean
        get() = valueOrDefault(Config.ACTIVE_KEY, true)

    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "Pure function should return something",
        Debt.TWENTY_MINS
    )

    override fun visitLambdaExpression(lambdaExpression: KtLambdaExpression) {
        bindingContext.takeIf { it != BindingContext.EMPTY }
            ?.let(lambdaExpression::getType)
            ?.getReturnTypeFromFunctionType()
            ?.isUnit()
            ?.let {
                val file = lambdaExpression.containingKtFile.name
                val name = lambdaExpression.parent.namedUnwrappedElement?.name ?: lambdaExpression.name ?: "expression"
                report(
                    CodeSmell(
                        issue,
                        Entity.from(lambdaExpression),
                        message = "Function $name in the file $file returns Unit."
                    )
                )
            }
        super.visitLambdaExpression(lambdaExpression)
    }

    override fun visitFunctionType(type: KtFunctionType) {
        bindingContext.takeIf { it != BindingContext.EMPTY }
            ?.let { type.returnTypeReference }
            ?.getAbbreviatedTypeOrType(bindingContext)
            ?.isUnit()
            ?.let {
                val file = type.containingKtFile
                val name = type.parent.namedUnwrappedElement?.name ?: type.name ?: "type"
                report(
                    CodeSmell(
                        issue, Entity.from(type),
                        message = "Function $name in the file ${file.name} returns Unit."
                    )
                )
            }
        super.visitFunctionType(type)
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        if (function.isMainFunction()) return

        bindingContext.takeIf { it != BindingContext.EMPTY }
            ?.get(BindingContext.FUNCTION, function)
            ?.returnType
            ?.takeIf(KotlinType::isUnit)
            ?.let {
                val file = function.containingKtFile
                report(
                    CodeSmell(
                        issue, Entity.from(function),
                        message = "Function ${function.name} in the file ${file.name} returns Unit."
                    )
                )
            }
        super.visitNamedFunction(function)
    }
}
