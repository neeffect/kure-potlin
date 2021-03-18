package pl.setblack.detekt.kurepotlin

import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.lint
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pl.setblack.detekt.kurepotlin.rules.ThrowExpression

class NoThrowExpressionSpec : Spek({


    describe("no throw expression rule") {
        val subject by memoized { ThrowExpression() }

        it("should detekt all exception side-effects") {
            val findings = subject.lint(impureThrowingCode)
            assertThat(findings).hasSize(1)
        }
    }
})

private const val impureThrowingCode: String =
    """
        fun throwingFn() = throw RuntimeException()
    """
