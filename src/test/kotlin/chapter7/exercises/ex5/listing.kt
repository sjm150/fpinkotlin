package chapter7.exercises.ex5

import chapter7.sec3.Pars
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

typealias Par<A> = (ExecutorService) -> Future<A>

//tag::init1[]
fun <A> sequence(ps: List<Par<A>>): Par<List<A>> =
    ps.fold(Pars.unit(emptyList())) { acc, par ->
        Pars.map2(acc, par) { la, a -> la + a }
    }
//end::init1[]
