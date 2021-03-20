package pl.setblack.detekt.kurepotlin.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.psiUtil.isAbstract
import pl.setblack.detekt.kurepotlin.isPureType

class ClassDefinition(config: Config = Config.empty) : Rule(config) {

    override val active: Boolean
        get() = valueOrDefault(Config.ACTIVE_KEY, false)

    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "Usage of object-oriented `class`",
        Debt.TWENTY_MINS
    )

    override fun visitClass(klass: KtClass) {
        if (!klass.isPureType() && !klass.isAbstract()) {
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
