package chapter3.exercises.ex12

import chapter3.List
import chapter3.foldLeft
import chapter3.foldRight
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
fun <A, B> foldLeftR(xs: List<A>, z: B, f: (B, A) -> B): B =
    foldRight(xs, { it }) { x, acc: (B) -> B ->
        { acc(f(it, x)) }
    }.invoke(z)

fun <A, B> foldRightL(xs: List<A>, z: B, f: (A, B) -> B): B =
    foldLeft(xs, { y -> y }) { acc: (B) -> B, x ->
        { acc(f(x, it)) }
    }.invoke(z)
// end::init[]

class Exercise12 : WordSpec({
    "list foldLeftR" should {
        "implement foldLeft functionality using foldRight" {
            foldLeftR(
                List.of(1, 2, 3, 4, 5),
                0,
                { x, y -> x + y }) shouldBe 15
        }
    }

    "list foldRightL" should {
        "implement foldRight functionality using foldLeft" {
            foldRightL(
                List.of(1, 2, 3, 4, 5),
                0,
                { x, y -> x + y }) shouldBe 15
        }
    }
})

fun main() {
    val l = List.of("a", "b", "c", "d", "e", "f")
    val f = { x: String, y: String -> "($x, $y)"}

    println(foldLeft(l, "z", f))
    println(foldRight(l, "z", f))

    println(foldLeftR(l, "z", f))
    println(foldRightL(l, "z", f))
}
