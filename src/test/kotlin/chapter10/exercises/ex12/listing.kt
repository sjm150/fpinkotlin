package chapter10.exercises.ex12

import arrow.Kind
import chapter10.Monoid
import chapter10.dual
import chapter10.endoMonoid

//tag::init1[]
interface Foldable<F> {

    fun <A, B> foldRight(fa: Kind<F, A>, z: B, f: (A, B) -> B): B =
        foldMap(fa, endoMonoid()) { a -> { b: B -> f(a, b) } }
            .invoke(z)

    fun <A, B> foldLeft(fa: Kind<F, A>, z: B, f: (B, A) -> B): B =
        foldMap(fa, dual(endoMonoid())) { a -> { b: B -> f(b, a) } }
            .invoke(z)

    fun <A, B> foldMap(fa: Kind<F, A>, m: Monoid<B>, f: (A) -> B): B =
        foldRight(fa, m.nil) { a, b -> m.combine(f(a), b) }
}
//end::init1[]
