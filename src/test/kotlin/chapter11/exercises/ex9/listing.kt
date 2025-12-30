package chapter11.exercises.ex9

import arrow.Kind
import chapter11.Monad

interface Listing<F, A> : Monad<F> {

    //tag::init0[]
    val f: (A) -> Kind<F, A>
    val x: Kind<F, A>
    val v: A
    //end::init0[]

    fun listing() {

        //tag::initl1[]
        compose(f, { a: A -> unit(a) })(v) == f(v);
        { a1: A -> flatMap(f(a1)) { a: A -> unit(a) } }(v) == f(v)
        flatMap(f(v)) { a -> unit(a) } == f(v)

        compose({ a: A -> unit(a) }, f)(v) == f(v);
        { a1: A -> flatMap({ a: A -> unit(a) }(a1), f) }(v) == f(v)
        flatMap({ a: A -> unit(a) }(v), f) == f(v)
        //end::initl1[]

        //tag::init2[]
        flatMap(x) { a -> unit(a) } == x
        flatMap(unit(v), f) == f(v)
        //end::init2[]
    }
}
