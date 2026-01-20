package chapter12.exercises.ex9

import arrow.Kind
import chapter12.Applicative
import chapter12.Composite
import chapter12.CompositePartialOf
import chapter12.fix

//tag::init1[]
fun <F, G> compose(
    AF: Applicative<F>,
    AG: Applicative<G>
): Applicative<CompositePartialOf<F, G>> = object : Applicative<CompositePartialOf<F, G>> {
    override fun <A> unit(a: A): Kind<CompositePartialOf<F, G>, A> =
        Composite(AF.unit(AG.unit(a)))

    override fun <A, B> apply(
        fab: Kind<CompositePartialOf<F, G>, (A) -> B>,
        fa: Kind<CompositePartialOf<F, G>, A>
    ): Kind<CompositePartialOf<F, G>, B> =
        Composite(
            AF.map2(fab.fix().value, fa.fix().value) { gab, ga -> AG.apply(gab, ga) }
        )
}
//end::init1[]
