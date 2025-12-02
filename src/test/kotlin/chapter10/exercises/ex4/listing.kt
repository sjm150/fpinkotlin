package chapter10.exercises.ex4

import chapter10.Monoid
import chapter10.intAdditionMonoid
import chapter10.intMultiplicationMonoid
import chapter8.Passed
import chapter8.SimpleRNG
import chapter8.Gen
import chapter8.Prop
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

//tag::init1[]
fun <A> monoidLaws(m: Monoid<A>, gen: Gen<A>): Prop {
    val identity = Prop.forAll(gen) {
        m.combine(it, m.nil) == it &&
            m.combine(m.nil, it) == it
    }

    val tripleGen = gen.flatMap { first ->
        gen.flatMap { second ->
            gen.map { third -> Triple(first, second, third) }
        }
    }

    val associativity = Prop.forAll(tripleGen) { (a1, a2, a3) ->
        m.combine(m.combine(a1, a2), a3) == m.combine(a1, m.combine(a2, a3))
    }

    return identity.and(associativity)
}
//end::init1[]

//tag::init2[]
class Exercise4 : WordSpec({
    val max = 100
    val count = 100
    val rng = SimpleRNG(42)
    val intGen = Gen.choose(-10000, 10000)

    "law of associativity" should {
        "be upheld using existing monoids" {
            monoidLaws(intAdditionMonoid, intGen)
                .check(max, count, rng) shouldBe Passed

            monoidLaws(intMultiplicationMonoid, intGen)
                .check(max, count, rng) shouldBe Passed
        }
    }
})
//end::init2[]
