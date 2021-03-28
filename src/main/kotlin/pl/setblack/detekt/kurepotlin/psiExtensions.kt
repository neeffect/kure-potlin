package pl.setblack.detekt.kurepotlin

import io.gitlab.arturbosch.detekt.api.AnnotationExcluder
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFunctionType
import org.jetbrains.kotlin.psi.KtLambdaExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtTypeAlias
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.psi.psiUtil.getAnnotationEntries
import org.jetbrains.kotlin.psi.psiUtil.parents

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
    AnnotationExcluder(containingKtFile, annotationSimpleNames).let { excluder ->
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
    AnnotationExcluder(containingKtFile, annotationSimpleNames).let { excluder ->
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
    AnnotationExcluder(containingKtFile, annotationSimpleNames)
        .shouldExclude(annotationEntries)
