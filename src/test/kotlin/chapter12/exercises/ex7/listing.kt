package chapter12.exercises.ex7

import arrow.Kind
import chapter11.Monad
import utils.SOLUTION_HERE

interface Listing<F, A> : Monad<F> {

    val fa: Kind<F, A>
    val fb: Kind<F, A>
    val f: (A, A) -> Kind<F, A>
    val ka: (A) -> Kind<F, A>

    fun listing1() {

        // left side
        map2(unit(Unit), fa) { _, a -> a }
        flatMap(unit(Unit)) { _ -> map(fa) { a -> a } }
        map(fa) { a -> a }
        flatMap(fa) { a -> unit(a) }
        fa

        // right side
        map2(fa, unit(Unit)) { a, _ -> a }
        flatMap(fa) { a -> map(unit(Unit)) { _ -> a } }
        flatMap(fa) { a -> unit(a) }
        fa

        fa == fa
    }
}
