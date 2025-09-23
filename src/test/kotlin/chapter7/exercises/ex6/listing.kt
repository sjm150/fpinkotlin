package chapter7.exercises.ex6

import chapter7.exercises.ex3.map2
import chapter7.exercises.ex5.sequence
import chapter7.exercises.ex4.Pars
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

typealias Par<A> = (ExecutorService) -> Future<A>

fun <A, B> Par<A>.map(f: (A) -> B): Par<B> =
    map2(this, Pars.unit {}) { a, _ -> f(a) }

//tag::init[]
fun <A> parFilter(
    sa: List<A>,
    f: (A) -> Boolean
): Par<List<A>> = Pars.fork {
    sequence(sa.map(Pars.asyncF { a: A -> a to f(a) }))
        .map {
            it.filter { (_, b) -> b }
                .map { (a, _) -> a }
        }
}
//end::init[]
