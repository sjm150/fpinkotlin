package chapter6.exercises.ex5

import chapter6.RNG
import chapter6.Rand
import chapter6.exercises.ex1.nonNegativeInt
import chapter6.map
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

class Exercise5 : WordSpec({

    //tag::init[]
    fun doubleR(): Rand<Double> =
        map(::nonNegativeInt) { it / -Int.MIN_VALUE.toDouble() }
    //end::init[]

    "doubleR" should {

        val unusedRng = object : RNG {
            override fun nextInt(): Pair<Int, RNG> = TODO()
        }

        """generate a max value approaching 1 based on
            Int.MAX_VALUE using Rand""" {

            val rngMax = object : RNG {
                override fun nextInt(): Pair<Int, RNG> =
                    Int.MAX_VALUE to unusedRng
            }

            val doubleRand = doubleR()
            doubleRand(rngMax) shouldBe (0.9999999995343387 to unusedRng)
        }

        "generate a min value of 0 based on 0 using Rand" {

            val rngMin = object : RNG {
                override fun nextInt(): Pair<Int, RNG> =
                    0 to unusedRng
            }

            val doubleRand = doubleR()
            doubleRand(rngMin) shouldBe (0.0 to unusedRng)
        }
    }
})
