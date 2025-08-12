package chapter5.exercises.ex1

import chapter3.List
import chapter3.Cons as ListCons
import chapter5.Cons
import chapter5.Empty
import chapter5.Stream
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

class Exercise1 : WordSpec({
    //tag::init[]
    fun <A> Stream<A>.toList(): List<A> {
        tailrec fun Stream<A>.toReverseList(acc: List<A>): List<A> =
            when (this) {
                is Cons -> tail().toReverseList(ListCons(head(), acc))
                is Empty -> acc
            }
        return toReverseList(List.empty()).reverse()
    }
    //end::init[]

    "Stream.toList" should {
        "force the stream into an evaluated list" {
            val s = Stream.of(1, 2, 3, 4, 5)
            s.toList() shouldBe List.of(1, 2, 3, 4, 5)
        }
    }
})
