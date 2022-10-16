package pl.setblack.detekt.kurepotlin

import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.lint
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pl.setblack.detekt.kurepotlin.rules.VariableDefinition

class NoVarSpec : Spek({
    setupKotlinEnvironment()

    describe("a rule") {

        val subject by memoized { VariableDefinition() }

        it("should detekt 2 vars") {
            val findings = subject.lint(impureCode)
            assertThat(findings).hasSize(2)
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
