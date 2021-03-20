package pl.setblack.detekt.kurepotlin.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtProperty

class VariableDefinition(config: Config = Config.empty) : Rule(config) {

    override val active: Boolean
        get() = valueOrDefault(Config.ACTIVE_KEY, true)

    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "Variables shouldn't be used in pure code",
        Debt.TWENTY_MINS
    )

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
