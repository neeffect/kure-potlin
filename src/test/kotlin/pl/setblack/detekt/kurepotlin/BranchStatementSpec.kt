package pl.setblack.detekt.kurepotlin

import io.gitlab.arturbosch.detekt.api.Finding
import io.gitlab.arturbosch.detekt.rules.setupKotlinEnvironment
import io.gitlab.arturbosch.detekt.test.lintWithContext
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pl.setblack.detekt.kurepotlin.rules.BranchStatement

class BranchStatementSpec : Spek({
    setupKotlinEnvironment()
    val env: KotlinCoreEnvironment by memoized()

    describe("a rule") {

        val subject by memoized { BranchStatement() }

        it("find branch that is a statement") {
            val messages = subject.lintWithContext(env, impureCode).map(Finding::message)
            assertThat(messages).containsExactly(
                "if statement in the file Test.kt is not an expression.",
                "if statement in the file Test.kt is not an expression.",
                "when statement in the file Test.kt is not an expression.",
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

        fun ifWithElseButStatement(a:Int) = run {
           if (a>5) {
                a+1
            } else {
                -a
            }
            a
        }

        fun whenStatement(a:Int) {
            when (a){
               a < 0 -> println("ok")
            }
        }

        fun whenExpression(a:Int) =
            when (a){
               0 -> "ok"
               else -> "nok"
            }
    """
