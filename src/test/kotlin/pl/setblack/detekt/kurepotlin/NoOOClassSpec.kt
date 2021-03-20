package pl.setblack.detekt.kurepotlin

import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.lint
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pl.setblack.detekt.kurepotlin.rules.ClassDefinition

class NoOOClassSpec : Spek({

    describe("object-oriented class code rule") {
        val subject by memoized { ClassDefinition() }

        it("should find impure classes") {
            val findings = subject.lint(impureClassesCode)
            assertThat(findings).hasSize(2)
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
        enum class EnumClass {
            ENUM
        }
    """
