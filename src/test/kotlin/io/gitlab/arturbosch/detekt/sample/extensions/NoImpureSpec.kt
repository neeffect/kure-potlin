package io.gitlab.arturbosch.detekt.sample.extensions

import io.gitlab.arturbosch.detekt.api.Finding
import io.gitlab.arturbosch.detekt.rules.setupKotlinEnvironment
import io.gitlab.arturbosch.detekt.sample.extensions.rules.ImpureCode
import io.gitlab.arturbosch.detekt.test.lint
import io.gitlab.arturbosch.detekt.test.lintWithContext
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class NoImpureSpec : Spek({
    setupKotlinEnvironment()

    val subject by memoized { ImpureCode() }
    val env: KotlinCoreEnvironment by memoized()

    describe("a simple test") {

        it("bla bla bla impure") {
            val findings = subject.lint(impureCode)
            assertThat(findings).hasSize(5)
        }

        it("find returns of Unit") {
            val messages = subject.lintWithContext(env, impureUnitsCode).map(Finding::message)
            assertThat(messages).containsExactly(
                "Function impureUnitExplicit in the file Test.kt returns Unit.",
                "Function impureUnitImplicit in the file Test.kt returns Unit.",
                "Function impureUnitExpression in the file Test.kt returns Unit.",
            )
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

const val impureUnitsCode: String =
    """
        fun impureUnitExplicit(): Unit { }
        
        fun impureUnitImplicit() { }
        
        fun impureUnitExpression() = Unit

        fun pureString() = "pure"
    """
