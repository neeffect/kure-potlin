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
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.psi.KtFunctionType
import org.jetbrains.kotlin.psi.KtLambdaExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.bindingContextUtil.getAbbreviatedTypeOrType
import org.jetbrains.kotlin.resolve.calls.callUtil.getType
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.isNothingOrNullableNothing
import org.jetbrains.kotlin.types.typeUtil.isUnit

/**
 * @requiresTypeResolution
 */
class ReturnUnit(config: Config = Config.empty) : Rule(config) {

    private val checkFunctionType: Boolean
        get() = valueOrDefault("checkFunctionType", true)

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
            ?.takeIf(KotlinType::isUnitNothingOrVoid)
            ?.let {
                val file = lambdaExpression.containingKtFile.name
                val name = lambdaExpression.parent.namedUnwrappedElement?.name ?: lambdaExpression.name ?: "expression"
                report(
                    CodeSmell(
                        issue,
                        Entity.from(lambdaExpression),
                        message = "Function $name in the file $file returns nothing."
                    )
                )
            }
        super.visitLambdaExpression(lambdaExpression)
    }

    override fun visitFunctionType(type: KtFunctionType) {
        bindingContext.takeIf { it != BindingContext.EMPTY }
            ?.let { type.returnTypeReference }
            ?.getAbbreviatedTypeOrType(bindingContext)
            ?.takeIf(KotlinType::isUnitNothingOrVoid)
            ?.takeIf { checkFunctionType }
            ?.let {
                val file = type.containingKtFile
                val name = type.parent.namedUnwrappedElement?.name ?: type.name ?: "type"
                report(
                    CodeSmell(
                        issue, Entity.from(type),
                        message = "Function $name in the file ${file.name} returns nothing."
                    )
                )
            }
        super.visitFunctionType(type)
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        bindingContext.takeIf { it != BindingContext.EMPTY }
            ?.get(BindingContext.FUNCTION, function)
            ?.returnType
            ?.takeUnless { function.isMainFunction() }
            ?.takeIf(KotlinType::isUnitNothingOrVoid)
            ?.let {
                val file = function.containingKtFile
                report(
                    CodeSmell(
                        issue, Entity.from(function),
                        message = "Function ${function.name} in the file ${file.name} returns nothing."
                    )
                )
            }
        super.visitNamedFunction(function)
    }
}

private fun KotlinType.isUnitNothingOrVoid(): Boolean =
    isUnit() || isNothingOrNullableNothing() || isVoid()

private fun KotlinType.isVoid(): Boolean =
    this.getJetTypeFqName(true) == "java.lang.Void"
