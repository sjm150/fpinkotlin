package chapter3.exercises.ex28

import chapter3.Branch
import chapter3.Leaf
import chapter3.Tree
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
fun <A, B> fold(ta: Tree<A>, l: (A) -> B, b: (B, B) -> B): B {
    fun go(ta: Tree<A>): B =
        when (ta) {
            is Leaf -> l(ta.value)
            is Branch -> b(go(ta.left), go(ta.right))
        }
    return go(ta)
}

fun <A> sizeF(ta: Tree<A>): Int =
    fold(ta, { 1 }) { l, r -> 1 + l + r }

fun maximumF(ta: Tree<Int>): Int =
    fold(ta, { it }) { l, r -> maxOf(l, r) }

fun <A> depthF(ta: Tree<A>): Int =
    fold(ta, { 0 }) { l, r -> 1 + maxOf(l, r) }

fun <A, B> mapF(ta: Tree<A>, f: (A) -> B): Tree<B> =
    fold(ta, { Leaf(f(it)) as Tree<B> }) { l, r -> Branch(l, r) }
// end::init[]

class Exercise28 : WordSpec({
    "tree fold" should {

        val tree = Branch(
            Branch(Leaf(1), Leaf(2)),
            Branch(
                Leaf(3),
                Branch(
                    Branch(Leaf(4), Leaf(5)),
                    Branch(
                        Leaf(21),
                        Branch(Leaf(7), Leaf(8))
                    )
                )
            )
        )
        "generalise size" {
            sizeF(tree) shouldBe 15
        }

        "generalise maximum" {
            maximumF(tree) shouldBe 21
        }

        "generalise depth" {
            depthF(tree) shouldBe 5
        }

        "generalise map" {
            mapF(tree) { it * 10 } shouldBe
                Branch(
                    Branch(Leaf(10), Leaf(20)),
                    Branch(
                        Leaf(30),
                        Branch(
                            Branch(Leaf(40), Leaf(50)),
                            Branch(
                                Leaf(210),
                                Branch(Leaf(70), Leaf(80))
                            )
                        )
                    )
                )
        }
    }
})
