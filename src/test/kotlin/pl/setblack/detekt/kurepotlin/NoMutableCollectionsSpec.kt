package pl.setblack.detekt.kurepotlin

import io.gitlab.arturbosch.detekt.rules.setupKotlinEnvironment
import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.lintWithContext
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pl.setblack.detekt.kurepotlin.rules.MutableCollectionsCode

class NoMutableCollectionsSpec : Spek({

    setupKotlinEnvironment()

    describe("a mutable collections rule") {
        val subject by memoized { MutableCollectionsCode() }
        val env: KotlinCoreEnvironment by memoized()

        it("should find mutable set usages") {
            val findings = subject.lintWithContext(env, mutableSetCode)
            assertThat(findings).hasSize(6)
        }
        it("should find mutable list usages") {
            val findings = subject.lintWithContext(env, mutableListCode)
            assertThat(findings).hasSize(6)
        }
        it("should find mutable map usages") {
            val findings = subject.lintWithContext(env, mutableMapCode)
            assertThat(findings).hasSize(6)
        }
    }
})

const val mutableSetCode: String =
    """
        val mutableSet = mutableSetOf<String>()

        fun mutableSetFn(mutableSet: MutableSet<String>): MutableSet<String> {
            val mutableProperty = emptySet<String>().toMutableSet()
            return mutableSetOf()
        }

        data class MutableClass(val mutableSet: MutableSet<String>)
    """

const val mutableListCode: String =
    """
        val mutableSet = mutableListOf<String>()

        fun mutableSetFn(mutableList: MutableList<String>): MutableList<String> {
            val mutableProperty = emptyList<String>().toMutableList()
            return mutableListOf()
        }

        data class MutableClass(val mutableList: MutableList<String>)
    """

const val mutableMapCode: String =
    """
        val mutableMap = mutableMapOf<String, String>()

        fun mutableSetFn(mutableMap: MutableMap<String, String>): MutableMap<String, String> {
            val mutableProperty = emptyMap<String, String>().toMutableMap()
            return mutableMapOf()
        }

        data class MutableClass(val mutableMap: MutableMap<String, String>)
    """
