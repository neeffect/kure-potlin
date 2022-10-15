package pl.setblack.detekt.kurepotlin.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.psi.KtReferenceExpression
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.bindingContextUtil.getAbbreviatedTypeOrType
import org.jetbrains.kotlin.resolve.calls.util.getType
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.supertypes

/**
 * @requiresTypeResolution
 */
class MutableCollections(config: Config = Config.empty) : Rule(config) {

    override val active: Boolean
        get() = valueOrDefault(Config.ACTIVE_KEY, true)

    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "Mutable collection usage",
        Debt.TWENTY_MINS
    )

    override fun visitReferenceExpression(expression: KtReferenceExpression) {
        val hasMutableCollectionType =
            bindingContext.takeUnless { it == BindingContext.EMPTY }
                ?.let(expression::getType)
                ?.isMutableCollection()
                ?: false
        if (hasMutableCollectionType) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(expression),
                    message = "Mutable collection used in the file ${expression.containingKtFile.name}."
                )
            )
        }
        super.visitReferenceExpression(expression)
    }

    override fun visitTypeReference(typeReference: KtTypeReference) {
        val hasMutableCollectionType = typeReference.getAbbreviatedTypeOrType(bindingContext)
            ?.isMutableCollection()
            ?: false
        if (hasMutableCollectionType) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(typeReference),
                    message = "Mutable collection used in the file ${typeReference.containingKtFile.name}."
                )
            )
        }
        super.visitTypeReference(typeReference)
    }
}

private val KotlinType.qualifiedClassName: String
    get() =
        getJetTypeFqName(false)

private fun KotlinType.isMutableCollection(): Boolean =
    supertypes()
        .map(KotlinType::qualifiedClassName)
        .any { it == mutableCollectionFullClassName || it == mutableMapFullClassName }
        .or(this.qualifiedClassName == mutableCollectionFullClassName)
        .or(this.qualifiedClassName == mutableMapFullClassName)

private const val mutableCollectionFullClassName = "kotlin.collections.MutableCollection"
private const val mutableMapFullClassName = "kotlin.collections.MutableMap"
