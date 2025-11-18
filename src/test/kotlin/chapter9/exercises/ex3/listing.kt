package chapter9.exercises.ex3

import chapter9.solutions.final.ParseError
import chapter9.solutions.final.Parser
import chapter9.solutions.final.ParserDsl

abstract class Listing : ParserDsl<ParseError>() {

    fun <A, B, C> map2(
        pa: Parser<A>,
        pb: Parser<B>,
        f: (A, B) -> C
    ): Parser<C> = (pa product { pb }).map { (a, b) -> f(a, b) }

    init {
        //tag::init1[]
        fun <A> many(pa: Parser<A>): Parser<List<A>> =
            succeed(listOf<A>()) or map2(pa, many(pa)) { a, la -> listOf(a) + la }
        //end::init1[]
    }
}
