package pl.setblack.detekt.kurepotlin

import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtClass

fun KtClass.isInlineClass() =
    hasModifier(KtTokens.INLINE_KEYWORD)

fun KtClass.isPureType(): Boolean =
    getClassOrInterfaceKeyword() == null ||
        isData() ||
        isEnum() ||
        isInlineClass() ||
        isInterface() ||
        isSealed()
