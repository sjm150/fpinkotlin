package chapter11.exercises.ex2

import arrow.Kind
import arrow.Kind2
import chapter11.Monad

//tag::init[]
data class State<S, out A>(val run: (S) -> Pair<A, S>) : Kind2<ForState, S, A>

class ForState {}

fun <S, A> Kind2<ForState, S, A>.fix(): State<S, A> = this as State<S, A>

interface StateMonad<S> : Monad<Kind<ForState, S>> {
    override fun <A> unit(a: A): Kind2<ForState, S, A>

    override fun <A, B> flatMap(
        fa: Kind2<ForState, S, A>,
        f: (A) -> Kind2<ForState, S, B>
    ): Kind2<ForState, S, B>
}

