package chapter10.exercises.ex6

import chapter10.dual
import chapter10.endoMonoid
import chapter10.foldMap

//tag::init1[]
fun <A, B> foldRight(la: Sequence<A>, z: B, f: (A, B) -> B): B =
    foldMap(la, endoMonoid()) { a ->
        { b: B -> f(a, b) }
    }.invoke(z)
//end::init1[]

//tag::init2[]
fun <A, B> foldLeft(la: Sequence<A>, z: B, f: (B, A) -> B): B =
    foldMap(la, dual(endoMonoid())) { a ->
        { b: B -> f(b, a) }
    }.invoke(z)
//end::init2[]
