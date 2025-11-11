package chapter8.exercises.ex14

import chapter8.Gen
import chapter8.Prop
import chapter8.SGen
import chapter8.sec4_1.run

val smallInt = Gen.choose(-10, 10)

fun List<Int>.prepend(i: Int) = listOf(i) + this

fun <A: Comparable<A>> sortedListOf(ga: Gen<A>): SGen<List<A>> =
    SGen { i -> Gen.listOfN(i, ga).map { it.sorted() } }

fun maxProp(): Prop = Prop.forAll(sortedListOf(smallInt)) {
    it.indices.all { i -> i == 0 || it[i - 1] <= it[i] }
}

fun main() {
    run(maxProp())
}
