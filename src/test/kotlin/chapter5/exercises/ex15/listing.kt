package chapter5.exercises.ex15

import chapter3.List
import chapter3.foldRight
import chapter4.None
import chapter4.Option
import chapter4.Some
import chapter5.Cons
import chapter5.Stream
import chapter5.toList
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import chapter3.Cons as ConsL
import chapter3.Nil as NilL

class Exercise15 : WordSpec({

    //tag::tails[]
    fun <A> Stream<A>.tails(): Stream<Stream<A>> =
        Stream.unfold(Some(this) as Option<Stream<A>>) { s ->
            s.map { it to if (it is Cons) Some(it.tail()) else None }
        }
    //end::tails[]

    fun <A, B> List<A>.map(f: (A) -> B): List<B> =
        foldRight(this, List.empty()) { a, acc -> ConsL(f(a), acc) }

    "Stream.tails" should {
        "return the stream of suffixes of the input sequence" {
            Stream.of(1, 2, 3)
                .tails()
                .toList()
                .map { it.toList() } shouldBe
                List.of(
                    ConsL(1, ConsL(2, ConsL(3, NilL))),
                    ConsL(2, ConsL(3, NilL)),
                    ConsL(3, NilL),
                    NilL
                )
        }
    }
})
