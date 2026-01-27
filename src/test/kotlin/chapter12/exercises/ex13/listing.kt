package chapter12.exercises.ex13

import arrow.Kind
import arrow.core.ForId
import arrow.core.Id
import arrow.core.extensions.id.comonad.extract
import arrow.core.fix
import chapter12.Applicative
import chapter12.Functor

fun idApplicative(): Applicative<ForId> = object : Applicative<ForId> {
    override fun <A> unit(a: A): Kind<ForId, A> = Id.just(a)

    override fun <A, B> map(
        fa: Kind<ForId, A>,
        f: (A) -> B
    ): Kind<ForId, B> = fa.fix().map(f)
}

//tag::init1[]
interface Traversable<F> : Functor<F> {
    //tag::ignore[]
    fun <G, A, B> traverse(
        fa: Kind<F, A>,
        AG: Applicative<G>,
        f: (A) -> Kind<G, B>
    ): Kind<G, Kind<F, B>> =
        sequence(map(fa, f), AG)

    fun <G, A> sequence(
        fga: Kind<F, Kind<G, A>>,
        AG: Applicative<G>
    ): Kind<G, Kind<F, A>> =
        traverse(fga, AG) { it }
    //end::ignore[]

    override fun <A, B> map(
        fa: Kind<F, A>,
        f: (A) -> B
    ): Kind<F, B> =
        traverse(fa, idApplicative()) { Id.just(f(it)) }.extract()
}
//end::init1[]
