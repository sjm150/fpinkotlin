package chapter4.exercises.ex4

import chapter3.Cons
import chapter3.List
import chapter4.None
import chapter4.Option
import chapter4.Some
import chapter4.foldRight
import chapter4.map2
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

//tag::init[]
fun <A> sequence(xs: List<Option<A>>): Option<List<A>> =
    xs.foldRight(Some(List.empty())) { x, acc ->
        map2(x, acc) { x, acc -> Cons(x, acc) }
    }
//end::init[]

class Exercise4 : WordSpec({

    "sequence" should {
        "turn a list of some options into an option of list" {
            val lo =
                List.of(Some(10), Some(20), Some(30))
            sequence(lo) shouldBe Some(List.of(10, 20, 30))
        }
        "turn a list of options containing none into a none" {
            val lo =
                List.of(Some(10), None, Some(30))
            sequence(lo) shouldBe None
        }
    }
})
