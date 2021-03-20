package pl.setblack.detekt.kurepotlin.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.resolve.BindingContext
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

    override fun visitNamedFunction(function: KtNamedFunction) {
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
