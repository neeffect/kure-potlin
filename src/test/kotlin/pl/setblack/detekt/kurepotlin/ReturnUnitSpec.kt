package pl.setblack.detekt.kurepotlin

import io.gitlab.arturbosch.detekt.api.Finding
import io.gitlab.arturbosch.detekt.rules.setupKotlinEnvironment
import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.lintWithContext
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pl.setblack.detekt.kurepotlin.rules.ReturnUnit

class ReturnUnitSpec : Spek({
    setupKotlinEnvironment()
    val env: KotlinCoreEnvironment by memoized()

    describe("a default rule") {

        val subject by memoized { ReturnUnit() }

        it("should find returns of Unit") {
            val messages = subject.lintWithContext(env, impureUnitCode).map(Finding::message)
            assertThat(messages).containsExactly(
                "Function ImpureUnitFunctionType in the file Test.kt returns nothing.",
                "Function impureUnitLambda in the file Test.kt returns nothing.",
                "Function impureParameter in the file Test.kt returns nothing.",
                "Function impureUnitExplicit in the file Test.kt returns nothing.",
                "Function impureUnitImplicit in the file Test.kt returns nothing.",
                "Function impureUnitExpression in the file Test.kt returns nothing.",
            )
        }

        it("should find returns of Nothing") {
            val messages = subject.lintWithContext(env, impureNothingCode).map(Finding::message)
            assertThat(messages).containsExactly(
                "Function ImpureNothingFunctionType in the file Test.kt returns nothing.",
                "Function ImpureNullableNothingFunctionType in the file Test.kt returns nothing.",
                "Function impureNothingLambda in the file Test.kt returns nothing.",
                "Function impureParameter in the file Test.kt returns nothing.",
                "Function impureNothingExplicit in the file Test.kt returns nothing."
            )
        }

        it("should find returns of Void") {
            val messages = subject.lintWithContext(env, impureVoidCode).map(Finding::message)
            assertThat(messages).containsExactly(
                "Function ImpureVoidFunctionType in the file Test.kt returns nothing.",
                "Function ImpureNullableVoidFunctionType in the file Test.kt returns nothing.",
                "Function impureVoidLambda in the file Test.kt returns nothing.",
                "Function impureParameter in the file Test.kt returns nothing.",
                "Function impureVoidExplicit in the file Test.kt returns nothing."
            )
        }
    }

    describe("a rule not checking function types") {

        val subject by memoized { ReturnUnit(TestConfig("checkFunctionType" to false)) }

        it("find returns of Unit") {
            val messages = subject.lintWithContext(env, impureUnitCode).map(Finding::message)
            assertThat(messages).containsExactly(
                "Function impureUnitLambda in the file Test.kt returns nothing.",
                "Function impureUnitExplicit in the file Test.kt returns nothing.",
                "Function impureUnitImplicit in the file Test.kt returns nothing.",
                "Function impureUnitExpression in the file Test.kt returns nothing.",
            )
        }
    }
})

private const val impureUnitCode: String =
    """
        typealias ImpureUnitFunctionType = () -> Unit
        
        val impureUnitLambda: ImpureUnitFunctionType = { }

        fun impureParameterFunction(impureParameter: () -> Unit) = "impure"

        fun impureUnitExplicit(): Unit { }
        
        fun impureUnitImplicit() { }
        
        fun impureUnitExpression() = Unit
        
        typealias PureFunctionType = () -> String

        fun pureString() = "pure"

        fun main(args: Array<String>) {
            // pure
        }
    """

private const val impureNothingCode: String =
    """
        typealias ImpureNothingFunctionType = () -> Nothing

        typealias ImpureNullableNothingFunctionType = () -> Nothing?
        
        val impureNothingLambda: ImpureNothingFunctionType = { throw Error() }

        fun impureParameterFunction(impureParameter: () -> Nothing) = "impure"

        fun impureNothingExplicit(): Nothing { }
    """

private const val impureVoidCode: String =
    """
        typealias ImpureVoidFunctionType = () -> Void
        
        typealias ImpureNullableVoidFunctionType = () -> Void?
        
        val impureVoidLambda: ImpureNullableVoidFunctionType = { null }

        fun impureParameterFunction(impureParameter: () -> Void) = "impure"

        fun impureVoidExplicit(): Void { }
    """
