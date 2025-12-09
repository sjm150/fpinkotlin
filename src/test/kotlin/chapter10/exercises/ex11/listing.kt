package chapter10.exercises.ex11

import chapter10.exercises.ex10.Part
import chapter10.exercises.ex10.Stub
import chapter10.exercises.ex10.wcMonoid
import chapter10.exercises.ex10.wordCount
import chapter10.foldMap
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

//tag::init1[]
fun wordCount(s: String): Int {
    val wc = foldMap(s.asSequence(), wcMonoid()) {
        if (it.isWhitespace()) Part("", 0, "")
        else Stub(it.toString())
    }

    return when (wc) {
        is Stub -> wc.chars.wordCount()
        is Part -> wc.ls.wordCount() + wc.words + wc.rs.wordCount()
    }
}
//end::init1[]

class Exercise11 : WordSpec({

    val words: List<String> =
        "lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do"
            .split(" ")

    "word count" should {
        "count words using balanced folding" {
            assertAll(Gen.list(Gen.from(words))) { ls ->
                val text = ls.joinToString(" ")
                println("${ls.size}: $text")
                wordCount(text) shouldBe ls.size
            }
        }
    }
})
