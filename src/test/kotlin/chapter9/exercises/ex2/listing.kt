package chapter9.exercises.ex2

import chapter8.Gen
import chapter8.Prop
import chapter8.Prop.Companion.forAll
import chapter9.solutions.final.ParseError
import chapter9.solutions.final.Parser
import chapter9.solutions.final.ParserDsl

abstract class Listing : ParserDsl<ParseError>() {

    private fun <A> equal(
        p1: Parser<A>,
        p2: Parser<A>,
        i: Gen<String>
    ): Prop =
        forAll(i) { run(p1, it) == run(p2, it) }

    // (a * b) * c = a * (b * c)
    fun <A, B, C> associative(
        pa: Parser<A>,
        pb: Parser<B>,
        pc: Parser<C>,
        i: Gen<String>
    ): Prop =
        equal(
            ((pa product { pb }) product { pc }).map { (p, c) -> Triple(p.first, p.second, c) },
            (pa product { (pb product { pc }) }).map { (a, p) -> Triple(a, p.first, p.second) },
            i
        )

    // f(a) * g(b) = (f @ g)(a * b) ?
    fun <A, B, C, D> distributive(
        pa: Parser<A>,
        pb: Parser<B>,
        f: (A) -> C,
        g: (B) -> D,
        i: Gen<String>
    ): Prop =
        equal(
            pa.map(f) product { pb.map(g) } ,
            (pa product { pb }).map { (a, b) -> f(a) to g(b) },
            i
        )
}
