package pl.setblack.detekt.kurepotlin

import io.gitlab.arturbosch.detekt.api.Finding
import io.gitlab.arturbosch.detekt.rules.setupKotlinEnvironment
import io.gitlab.arturbosch.detekt.test.lintWithContext
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pl.setblack.detekt.kurepotlin.rules.MissingElse

class MissingElseSpec : Spek({
    setupKotlinEnvironment()
    val env: KotlinCoreEnvironment by memoized()

    describe("a rule") {

        val subject by memoized { MissingElse() }

        it("find if without else") {
            val messages = subject.lintWithContext(env, impureCode).map(Finding::message)
            assertThat(messages).containsExactly(
                "if statement in the file Test.kt has no else.",
            )
        }
    }
})

private const val impureCode: String =
    """
       fun ifWithoutElse(a:Int) = run {
            if (a > 5) {
                println(a)
            }
            "ok"
       }

        fun ifWithElse(a:Int) = run {
           if (a>5) {
                a+1
            } else {
                -a
            }
            a
        }
    """
