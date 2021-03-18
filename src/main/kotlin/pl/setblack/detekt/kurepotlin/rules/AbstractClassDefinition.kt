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

class AbstractClassDefinition(config: Config = Config.empty) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "Usage of object-oriented `abstract class`",
        Debt.TWENTY_MINS
    )

    override fun visitClass(klass: KtClass) {
        if (!klass.isPureType() && klass.isAbstract()) {
            val file = klass.containingKtFile
            report(
                CodeSmell(
                    issue,
                    Entity.from(klass),
                    message = "The file ${file.name} contains object-oriented `abstract class`."
                )
            )
        }
        super.visitClass(klass)
    }
}
