package chapter8.exercises.ex9

import chapter8.RNG

typealias TestCases = Int

sealed class Result {
    abstract fun isFalsified(): Boolean
}

object Passed : Result() {
    override fun isFalsified(): Boolean = false
}

typealias SuccessCount = Int
typealias FailedCase = String

data class Falsified(
    val failure: FailedCase,
    val successes: SuccessCount
) : Result() {
    override fun isFalsified(): Boolean = true
}

//tag::init[]
data class Prop(val run: (TestCases, RNG) -> Result) {
    fun and(p: Prop): Prop = Prop { n, rng ->
        run(n, rng).takeIf { it.isFalsified() }
            ?: p.run(n, rng).takeIf { it.isFalsified() }
            ?: Passed
    }

    fun or(p: Prop): Prop = Prop { n, rng ->
        val r = run(n, rng)
        val pr = p.run(n, rng)
        if (r is Falsified && pr is Falsified) {
            Falsified(
                listOf(r.failure, pr.failure).joinToString("\n"),
                maxOf(r.successes, pr.successes)
            )
        } else {
            Passed
        }
    }
}
//end::init[]
