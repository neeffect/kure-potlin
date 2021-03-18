package pl.setblack.detekt.kurepotlin

import io.gitlab.arturbosch.detekt.rules.setupKotlinEnvironment
import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.lint
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pl.setblack.detekt.kurepotlin.rules.ReturnStatement

class NoReturnStatementSpec : Spek({
    setupKotlinEnvironment()

    describe("a rule") {

        val subject by memoized { ReturnStatement() }
        val env: KotlinCoreEnvironment by memoized()

        it("should detect 1 return statement") {
            val findings = subject.lint(impureCode)
            assertThat(findings).hasSize(1)
        }
    }
})

private const val impureCode: String =
    """
            class NoImpure  {

                fun impure ():Int {
                    var  x = 1
                    x = x + 1
                    return x
                }

                fun impureLoop() : Int  {
                    var z= 0
                    for (i in 1..10 ) {
                            z+=i
                    }
                    while (z<100) {
                            z++
                    }
                }

                fun pure (x:Int ):Int = x + 1 
            }
        """
