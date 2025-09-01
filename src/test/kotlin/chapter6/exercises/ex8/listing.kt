package chapter6.exercises.ex8

import chapter6.Rand
import chapter6.exercises.ex1.nonNegativeInt
import chapter6.rng1
import chapter6.unit
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

class Exercise8 : WordSpec({

    //tag::init[]
    fun <A, B> flatMap(f: Rand<A>, g: (A) -> Rand<B>): Rand<B> = { rng ->
        val (a, r) = f(rng)
        g(a).invoke(r)
    }
    //end::init[]

    fun nonNegativeIntLessThan(n: Int): Rand<Int> =
        flatMap(::nonNegativeInt) { i ->
            val mod = i % n
            if (i + n - 1 - mod >= 0) unit(mod)
            else nonNegativeIntLessThan(n)
        }

    "flatMap" should {
        "pass along an RNG" {

            val result =
                flatMap(
                    unit(1),
                    { i -> unit(i.toString()) })(rng1)

            result.first shouldBe "1"
            result.second shouldBe rng1
        }
    }

    "nonNegativeIntLessThan" should {
        "return a non negative int less than n" {

            val result =
                nonNegativeIntLessThan(10)(rng1)

            result.first shouldBe 1
        }
    }
})
