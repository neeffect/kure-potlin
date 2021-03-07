package io.gitlab.arturbosch.detekt.sample.extensions.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtLoopExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtReturnExpression

class ImpureCode : Rule() {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "Impure code",
        Debt.TWENTY_MINS
    )

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
    }

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
    //    override fun visitKtElement(element: KtElement) {
//        println(element)
//        super.visitKtElement(element)
//    }

//    override fun visitTypeReference(typeReference: KtTypeReference) {
//
//        super.visitTypeReference(typeReference)
//    }

//    override fun visitReferenceExpression(expression: KtReferenceExpression) {
//        println("ref expression: $expression ${expression.text}")
//        super.visitReferenceExpression(expression)
//    }

//    override fun visitBinaryExpression(expression: KtBinaryExpression) {
//        println("binary expression: ${expression.text} ${expression.operationToken}")
//        if (expression.operationToken.toString() == "EQ") {
//            val file  = expression.containingKtFile
//            report(
//                CodeSmell(issue, Entity.atPackageOrFirstDecl(file),
//                    message = "The file ${file.name} contains assignment to `var`iable.")
//            )
//        }
//        super.visitBinaryExpression(expression)
//    }

    override fun visitProperty(property: KtProperty) {
//        println("property: $property is var: ${property.isVar} ${property.name}")
        if (property.isVar) {
            val file = property.containingKtFile
            report(
                CodeSmell(
                    issue, Entity.from(property),
                    message = "The file ${file.name} contains `var`iable."
                )
            )
        }
        super.visitProperty(property)
    }

    override fun visitReturnExpression(expression: KtReturnExpression) {
//        println("return: ${expression.returnKeyword}")
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
