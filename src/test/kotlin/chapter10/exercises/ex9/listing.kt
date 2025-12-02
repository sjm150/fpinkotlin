package chapter10.exercises.ex9

import chapter10.Monoid
import chapter10.foldMap
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

data class SegmentInfo(
    val min: Int,
    val max: Int,
    val ordered: Boolean
) {
    companion object {
        fun single(i: Int): SegmentInfo = SegmentInfo(i, i, true)
    }
}

val segmentInfoMonoid: Monoid<SegmentInfo> = object : Monoid<SegmentInfo> {
    override fun combine(
        a1: SegmentInfo,
        a2: SegmentInfo
    ): SegmentInfo =
        SegmentInfo(
            min = minOf(a1.min, a2.min),
            max = maxOf(a1.max, a2.max),
            ordered = a1.ordered && a2.ordered && a1.max <= a2.min
        )

    override val nil: SegmentInfo
        get() = SegmentInfo(Int.MAX_VALUE, Int.MIN_VALUE, true)
}

//tag::init1[]
fun ordered(ints: Sequence<Int>): Boolean =
    foldMap(ints, segmentInfoMonoid) { SegmentInfo.single(it) }.ordered
//end::init1[]

class Exercise9 : WordSpec({
    "ordered using balanced fold" should {
        "verify ordering ordered list" {
            ordered(sequenceOf(1, 2, 3, 4, 5, 6, 7, 8, 9)) shouldBe true
        }

        "fail verification of unordered list" {
            ordered(sequenceOf(3, 2, 5, 6, 1, 4, 7, 9, 8)) shouldBe false
        }
    }
})
