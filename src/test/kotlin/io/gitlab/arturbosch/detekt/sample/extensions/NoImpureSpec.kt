package io.gitlab.arturbosch.detekt.sample.extensions

import io.gitlab.arturbosch.detekt.sample.extensions.rules.ImpureCode
import io.gitlab.arturbosch.detekt.test.lint
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class NoImpureSpec : Spek({

    val subject by memoized { ImpureCode() }

    describe("a simple test") {

        it("bla bla bla impure") {
            val findings = subject.lint(impureCode)
            assertThat(findings).hasSize(5)
        }
    }
})

const val impureCode: String =
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
