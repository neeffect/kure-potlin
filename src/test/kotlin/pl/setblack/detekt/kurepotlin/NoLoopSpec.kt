package pl.setblack.detekt.kurepotlin

import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.lint
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pl.setblack.detekt.kurepotlin.rules.LoopDefinition

class NoLoopSpec : Spek({
    setupKotlinEnvironment()

    describe("a rule") {

        val subject by memoized { LoopDefinition() }

        it("should detect 2 loops") {
            val findings = subject.lint(impureCode)
            assertThat(findings).hasSize(2)
        }
    }
})

private const val impureCode: String =
    """
            class NoImpure  {

                fun impureLoop() {
                    for (i in 1..10 ) {
                    }
                    while (z<100) {
                    }

                }
            }
        """


