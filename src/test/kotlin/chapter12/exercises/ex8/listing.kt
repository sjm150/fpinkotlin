package chapter12.exercises.ex8

import arrow.Kind
import chapter12.Applicative
import chapter12.Product
import chapter12.ProductPartialOf
import chapter12.fix

//tag::init1[]
fun <F, G> product(
    AF: Applicative<F>,
    AG: Applicative<G>
): Applicative<ProductPartialOf<F, G>> = object : Applicative<ProductPartialOf<F, G>> {
    override fun <A> unit(a: A): Kind<ProductPartialOf<F, G>, A> =
        Product(AF.unit(a) to AG.unit(a))

    override fun <A, B, C> map2(
        fa: Kind<ProductPartialOf<F, G>, A>,
        fb: Kind<ProductPartialOf<F, G>, B>,
        f: (A, B) -> C
    ): Kind<ProductPartialOf<F, G>, C> {
        val (af, bf) = fa.fix().value
        val (ag, bg) = fb.fix().value
        return Product(AF.map2(af, ag, f) to AG.map2(bf, bg, f))
    }
}
//end::init1[]
