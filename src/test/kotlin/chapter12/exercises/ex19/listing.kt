package chapter12.exercises.ex19

import arrow.Kind
import chapter11.Monad
import chapter12.Applicative
import chapter12.Composite
import chapter12.CompositePartialOf
import chapter12.Traversable
import chapter12.fix

fun <F> applicative(m: Monad<F>) = object : Applicative<F> {
    override fun <A> unit(a: A): Kind<F, A> = m.unit(a)

    override fun <A, B, C> map2(
        fa: Kind<F, A>,
        fb: Kind<F, B>,
        f: (A, B) -> C
    ): Kind<F, C> = m.map2(fa, fb, f)
}

//tag::init[]
fun <G, H> composeM(
    MG: Monad<G>,
    MH: Monad<H>,
    TH: Traversable<H>
): Monad<CompositePartialOf<G, H>> = object : Monad<CompositePartialOf<G, H>> {
    override fun <A> unit(a: A): Kind<CompositePartialOf<G, H>, A> =
        Composite(MG.unit(MH.unit(a)))

    override fun <A, B> flatMap(
        fa: Kind<CompositePartialOf<G, H>, A>,
        f: (A) -> Kind<CompositePartialOf<G, H>, B>
    ): Kind<CompositePartialOf<G, H>, B> {
        fun <A, B> Kind<G, A>.map(f: (A) -> B) = MG.map(this, f)
        fun <A, B> Kind<H, A>.map(f: (A) -> B) = MH.map(this, f)
        fun <A> Kind<G, Kind<G, A>>.join() = MG.join(this)
        fun <A> Kind<H, Kind<H, A>>.join() = MH.join(this)

        return Composite(
            fa.fix().value
                .map { ha ->
                    TH.sequence(
                        ha.map { f(it).fix().value },
                        applicative(MG)
                    )
                }
                .join()
                .map { it.join() }
        )
    }
}
//end::init[]
