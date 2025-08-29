package chapter6.exercises.ex4

import chapter3.Cons
import chapter3.List
import chapter6.RNG
import chapter6.rng1
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

class Exercise4 : WordSpec({

    //tag::init[]
    fun ints(count: Int, rng: RNG): Pair<List<Int>, RNG> =
        if (count > 0) {
            val (head, r1) = rng.nextInt()
            val (tail, r2) = ints(count - 1, r1)
            Cons(head, tail) to r2
        } else {
            List.empty<Int>() to rng
        }
    //end::init[]

    "ints" should {
        "generate a list of ints of a specified length" {

            ints(5, rng1) shouldBe (List.of(1, 1, 1, 1, 1) to rng1)
        }
    }
})
