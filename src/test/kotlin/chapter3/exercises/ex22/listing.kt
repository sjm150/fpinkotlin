package chapter3.exercises.ex22

import chapter3.Cons
import chapter3.List
import chapter3.Nil
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
fun <A> zipWith(xa: List<A>, xb: List<A>, f: (A, A) -> A): List<A> {
    fun go(xa: List<A>, xb: List<A>): List<A> =
        if (xa is Cons && xb is Cons)
            Cons(f(xa.head, xb.head), go(xa.tail, xb.tail))
        else
            Nil
    return go(xa, xb)
}
// end::init[]

class Exercise22 : WordSpec({
    "list zipWith" should {
        "apply a function to elements of two corresponding lists" {
            zipWith(
                List.of(1, 2, 3),
                List.of(4, 5, 6)
            ) { x, y -> x + y } shouldBe List.of(5, 7, 9)
        }
    }
})
