package chapter8.exercises.ex13

import chapter8.Gen
import chapter8.Prop
import chapter8.SGen
import chapter8.sec4_1.run

fun main() {
    //tag::init1[]
    fun <A> nonEmptyListOf(ga: Gen<A>): SGen<List<A>> =
        SGen { i -> Gen.listOfN(i + 1, ga) }
    //end::init1[]

    val smallInt = Gen.choose(-10, 10)

    //tag::init2[]
    fun maxProp(): Prop =
        Prop.forAll(nonEmptyListOf(smallInt)) { a ->
            val mx = a.max() ?: throw IllegalStateException()
            a.all { it <= mx }
        }
    //end::init2[]
    run(maxProp())
}
