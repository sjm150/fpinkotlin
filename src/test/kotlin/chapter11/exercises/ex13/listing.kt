package chapter11.exercises.ex13

import arrow.Kind
import chapter11.Monad

interface Listing<F, A> : Monad<F> {

    //tag::initA[]
    val f: (A) -> Kind<F, A>
    val g: (A) -> Kind<F, A>
    val x: Kind<F, A>
    val y: Kind<F, Kind<F, Kind<F, A>>>
    val z: (Kind<F, Kind<F, A>>) -> Kind<F, Kind<F, A>>
    //end::initA[]

    fun associative() {
        //tag::init1[]
        flatMap(flatMap(x, f), g) ==
            flatMap(x) { a -> flatMap(f(a), g) }
        //end::init1[]

        // left term
        flatMap(join(map(x, f)), g)
        join(map(join(map(x, f)), g))

        join(map(join(map(x, ::unit)), g))
        join(map(join(unit(x)), g))
        join(map(x, g))
        join(map(x, ::unit))
        join(unit(x))
        x

        // right term
        join(map(x) { a -> flatMap(f(a), g) })
        join(map(x) { a -> join(map(f(a), g)) })

        join(map(x) { a -> join(map(unit(a), g)) })
        join(map(x) { a -> join(map(unit(a), ::unit)) })
        join(map(x) { a -> join(unit(unit(a))) })
        join(map(x) { a -> unit(a) })
        join(unit(x))
        x

        //tag::init5[]
        join(unit(x)) ==
            join(map(x) { unit(it) })
        //end::init5[]
    }
}
