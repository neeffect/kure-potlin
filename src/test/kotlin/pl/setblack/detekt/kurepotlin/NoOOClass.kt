package pl.setblack.detekt.kurepotlin

import pl.setblack.detekt.kurepotlin.rules.ObjectOrientedClassCode
import io.gitlab.arturbosch.detekt.test.lint
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class NoOOClass : Spek({


    describe("object-oriented class code rule") {
        val subject by memoized { ObjectOrientedClassCode() }

        it("should find impure classes") {
            val findings = subject.lint(impureClassesCode)
            assertThat(findings).hasSize(3)
        }
    }
})

private const val impureClassesCode: String =
    """
        // impure
        class OOClass
        private class PrivateOOClass
        abstract class AbstractClass

        // pure
        data class DataClass(val v: String)
        inline class InlineClass(val v: String)
        object Obj
        interface Interface
        sealed class SealedClass
    """
