package chapter11.exercises.ex10

import arrow.Kind
import arrow.core.ForOption
import arrow.core.None
import arrow.core.Some
import chapter11.Monad

interface Listing<A> : Monad<ForOption> {

    val v: A
    val f: (A) -> Kind<ForOption, A>

    fun exercise() {
        // left identity
        flatMap(None) { a: A -> unit(a) } == None
        None == None

        flatMap(Some(v)) { a: A -> unit(a) } == Some(v)
        unit(v) == Some(v)
        Some(v) == Some(v)

        // right identity
        flatMap(unit(v)) { a: A -> None } == { a: A -> None }(v)
        flatMap(Some(v)) { a: A -> None } == None
        None == None

        flatMap(unit(v)) { a: A -> Some(a) } == { a: A -> Some(a) }(v)
        flatMap(Some(v)) { a: A -> Some(a) } == Some(v)
        Some(v) == Some(v)

    }
}
