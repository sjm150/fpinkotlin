package chapter6.exercises.ex7

import chapter3.Cons
import chapter3.List
import chapter3.Nil
import chapter3.foldRight
import chapter6.RNG
import chapter6.Rand
import chapter6.exercises.ex6.map2
import chapter6.rng1
import chapter6.unit
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

class Exercise7 : WordSpec({

    //tag::init[]
    fun <A> sequence(fs: List<Rand<A>>): Rand<List<A>> = { rng ->
        when (fs) {
            is Cons -> map2(fs.head, sequence(fs.tail)) { a, la ->
                Cons(a, la)
            }.invoke(rng)
            is Nil -> Nil to rng
        }
    }
    //end::init[]

    //tag::init2[]
    fun <A> sequence2(fs: List<Rand<A>>): Rand<List<A>> =
        foldRight(fs, { rng: RNG -> List.empty<A>() to rng }) { ra, acc ->
            map2(ra, acc) { a, la -> Cons(a, la) }
        }
    //end::init2[]

    fun ints2(count: Int, rng: RNG): Pair<List<Int>, RNG> {
        fun gen(count: Int): List<Rand<Int>> =
            if (count > 0) Cons(RNG::nextInt, gen(count - 1))
            else Nil
        return sequence2(gen(count)).invoke(rng)
    }

    "sequence" should {

        "combine the results of many actions using recursion" {

            val combined: Rand<List<Int>> =
                sequence(
                    List.of(
                        unit(1),
                        unit(2),
                        unit(3),
                        unit(4)
                    )
                )

            combined(rng1).first shouldBe
                List.of(1, 2, 3, 4)
        }

        """combine the results of many actions using
            foldRight and map2""" {

            val combined2: Rand<List<Int>> =
                sequence2(
                    List.of(
                        unit(1),
                        unit(2),
                        unit(3),
                        unit(4)
                    )
                )

            combined2(rng1).first shouldBe
                List.of(1, 2, 3, 4)
        }
    }

    "ints" should {
        "generate a list of ints of a specified length" {
            ints2(4, rng1).first shouldBe
                List.of(1, 1, 1, 1)
        }
    }
})
