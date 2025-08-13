package chapter5.exercises.ex14

import chapter4.Some
import chapter5.Stream
import chapter5.exercises.ex13.takeWhile
import chapter5.exercises.ex13.zipAll
import chapter5.exercises.ex4.forAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

class Exercise14 : WordSpec({

    //tag::startswith[]
    fun <A> Stream<A>.startsWith(that: Stream<A>): Boolean =
        zipAll(that)
            .takeWhile { !it.second.isEmpty() }
            .forAll { (f, s) -> f is Some && s is Some && f.get == s.get }
    //end::startswith[]

    "Stream.startsWith" should {
        "detect if one stream is a prefix of another" {
            Stream.of(1, 2, 3).startsWith(
                Stream.of(1, 2)
            ) shouldBe true
        }
        "detect if one stream is not a prefix of another" {
            Stream.of(1, 2, 3).startsWith(
                Stream.of(2, 3)
            ) shouldBe false
        }
    }
})
