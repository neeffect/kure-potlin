package pl.setblack.detekt.kurepotlin.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClass

class ObjectOrientedClassCode(config: Config = Config.empty) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "Usage of object-oriented `class` or `abstract class`",
        Debt.TWENTY_MINS
    )

    override fun visitClass(klass: KtClass) {
        val isPureType =
            klass.isData() ||
                klass.isEnum() ||
                klass.isInline() ||
                klass.isInterface() ||
                klass.isSealed() ||
                klass.isValue()
        if (!isPureType) {
            val file = klass.containingKtFile
            report(
                CodeSmell(
                    issue,
                    Entity.from(klass),
                    message = "The file ${file.name} contains object-oriented `class`."
                )
            )
        }
        super.visitClass(klass)
    }
}
