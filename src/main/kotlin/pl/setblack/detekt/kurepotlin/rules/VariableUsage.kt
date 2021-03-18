package pl.setblack.detekt.kurepotlin.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtProperty

class VariableUsage(config: Config = Config.empty) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "Variables shouldn't be used in pure code",
        Debt.TWENTY_MINS
    )

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
}
