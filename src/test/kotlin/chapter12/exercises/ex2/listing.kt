package chapter12.exercises.ex2

import arrow.Kind
import chapter12.Functor

//tag::init1[]
interface Applicative<F> : Functor<F> {

    fun <A, B> apply(
        fab: Kind<F, (A) -> B>,
        fa: Kind<F, A>
    ): Kind<F, B> =
        map2(fab, fa) { ab, a -> ab(a) }

    fun <A> unit(a: A): Kind<F, A>

    override fun <A, B> map(
        fa: Kind<F, A>,
        f: (A) -> B
    ): Kind<F, B> =
        apply(unit(f), fa)

    fun <A, B, C> map2(
        fa: Kind<F, A>,
        fb: Kind<F, B>,
        f: (A, B) -> C
    ): Kind<F, C> =
        apply(apply(unit { a -> { b -> f(a, b) } }, fa), fb)
}
//end::init1[]
