package pl.setblack.detekt.kurepotlin

import io.gitlab.arturbosch.detekt.api.Finding
import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.lintWithContext
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pl.setblack.detekt.kurepotlin.rules.ReturnUnit
import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.CONSTRUCTOR
import kotlin.annotation.AnnotationTarget.EXPRESSION
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.FILE
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.LOCAL_VARIABLE
import kotlin.annotation.AnnotationTarget.PROPERTY
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.annotation.AnnotationTarget.PROPERTY_SETTER
import kotlin.annotation.AnnotationTarget.TYPE
import kotlin.annotation.AnnotationTarget.TYPEALIAS
import kotlin.annotation.AnnotationTarget.TYPE_PARAMETER
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

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

        it("should find dsl") {
            val messages = subject.lintWithContext(env, dslCode).map(Finding::message)
            assertThat(messages).containsExactly(
                "Function dslParameter in the file Test.kt returns nothing.",
                "Function dslParameter in the file Test.kt returns nothing.",
                "Function nonDslParameter in the file Test.kt returns nothing.",
                "Function nonDslParameter in the file Test.kt returns nothing."
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

    describe("a rule with dsl ignored") {
        val subject by memoized { ReturnUnit(TestConfig("ignoreDsl" to true)) }

        it("should find only non-dsl") {
            val messages = subject.lintWithContext(env, dslCode).map(Finding::message)
            assertThat(messages).containsExactly(
                "Function nonDslParameter in the file Test.kt returns nothing.",
                "Function nonDslParameter in the file Test.kt returns nothing."
            )
        }
    }

    describe("a rule not checking function types") {

        val subject by memoized { ReturnUnit(TestConfig("ignoreFunctionType" to true)) }

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

    describe("a rule suppressed by IO annotation") {

        val subject by memoized {
            ReturnUnit(TestConfig("ignoredAnnotations" to "IO"))
        }

        it("find returns of Unit") {
            val messages = subject.lintWithContext(env, impureUnitSuppressedCode).map(Finding::message)
            assertThat(messages).isEmpty()
        }
    }
})

@Target(
    CLASS, ANNOTATION_CLASS, TYPE_PARAMETER, PROPERTY, FIELD, LOCAL_VARIABLE, VALUE_PARAMETER, CONSTRUCTOR,
    FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER, TYPE, EXPRESSION, FILE, TYPEALIAS
)
@Retention(AnnotationRetention.SOURCE)
private annotation class IO

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

        fun main(vararg args: String) {
            // pure
        }
    """

private const val dslCode: String =
    """
        fun dslParameterFunction(dslParameter: DslBuilder.() -> Unit = {}) = "impure"
        
        fun nonDslParameterFunction(nonDslParameter: DslBuilder.(String) -> Unit = {}) = "impure"

        class DslBuilder { }
    """

private const val impureUnitSuppressedCode: String =
    """
        import pl.setblack.detekt.kurepotlin.IO
    
        @IO
        typealias ImpureUnitFunctionType = () -> Unit
        
        val impureUnitLambda: ImpureUnitFunctionType = @IO { }
        
        @IO
        val anotherImpureUnitLambda: ImpureUnitFunctionType = { }
        
        @IO
        fun impureUnitLambdaFunction() = { }

        fun impureParameterFunction(@IO impureParameter: () -> Unit) = "impure"
        
        fun anotherImpureParameterFunction(impureParameter: @IO () -> Unit) = "impure"

        @IO
        fun impureUnitExplicit(): Unit { }

        @IO
        fun impureUnitImplicit() { }

        @IO
        fun impureUnitExpression() = Unit
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
