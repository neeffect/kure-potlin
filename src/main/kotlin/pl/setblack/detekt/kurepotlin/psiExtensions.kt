package pl.setblack.detekt.kurepotlin

import io.gitlab.arturbosch.detekt.api.AnnotationExcluder
import io.gitlab.arturbosch.detekt.rules.hasAnnotation
import io.gitlab.arturbosch.detekt.rules.isPublicNotOverridden
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFunctionType
import org.jetbrains.kotlin.psi.KtLambdaExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtTypeAlias
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.psi.psiUtil.getAnnotationEntries
import org.jetbrains.kotlin.psi.psiUtil.parents
import org.jetbrains.kotlin.resolve.BindingContext

fun KtClass.isInlineClass() =
    hasModifier(KtTokens.INLINE_KEYWORD)

fun KtClass.isPureType(): Boolean =
    getClassOrInterfaceKeyword() == null ||
            isData() ||
            isEnum() ||
            isInlineClass() ||
            isInterface() ||
            isSealed()

fun KtLambdaExpression.isAnnotatedWithAnyOf(annotationSimpleNames: List<String>) =
    AnnotationExcluder(containingKtFile, annotationSimpleNames.toReg(), BindingContext.EMPTY).let { excluder ->
        excluder.shouldExclude(getAnnotationEntries()) ||
                parents.any { parent ->
                    when (parent) {
                        is KtProperty -> excluder.shouldExclude(parent.annotationEntries)
                        is KtNamedFunction -> excluder.shouldExclude(parent.annotationEntries)
                        else -> false
                    }
                }
    }

fun KtFunctionType.isAnnotatedWithAnyOf(annotationSimpleNames: List<String>) =
    AnnotationExcluder(containingKtFile, annotationSimpleNames.toReg(), BindingContext.EMPTY).let { excluder ->
        parents.any { parent ->
            when (parent) {
                is KtTypeAlias -> excluder.shouldExclude(parent.annotationEntries)
                is KtParameter -> excluder.shouldExclude(parent.annotationEntries)
                is KtTypeReference -> excluder.shouldExclude(parent.annotationEntries)
                else -> false
            }
        }
    }

fun KtNamedFunction.isAnnotatedWithAnyOf(annotationSimpleNames: List<String>) =
    AnnotationExcluder(
        containingKtFile,
        annotationSimpleNames.toReg(),
        BindingContext.EMPTY).shouldExclude(annotationEntries)


fun List<String>.toReg(): List<Regex> = this.map {
    it.replace(".", "\\.").replace("*", ".*").toRegex()
}

fun KtNamedFunction.isMainFunction() = hasMainSignature() && (this.isTopLevel || isMainInsideObject())

private fun KtNamedFunction.isMainInsideObject() =
    this.name == "main" &&
            this.isPublicNotOverridden() &&
            this.parent?.parent is KtObjectDeclaration &&
            this.hasAnnotation("JvmStatic", "kotlin.jvm.JvmStatic")

private fun KtNamedFunction.hasMainSignature() =
    this.name == "main" && this.isPublicNotOverridden() && this.hasMainParameter()

private fun KtNamedFunction.hasMainParameter() =
    valueParameters.isEmpty()
            || (valueParameters.size == 1 && valueParameters[0].typeReference?.text == "Array<String>")
            || (valueParameters.size == 1 && valueParameters[0].isVarArg && valueParameters[0].typeReference?.text == "String")
