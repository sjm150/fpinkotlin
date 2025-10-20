package chapter8.exercises.ex8

import chapter8.RNG
import chapter8.State
import chapter8.double

data class Gen<A>(val sample: State<RNG, A>) {
    companion object {

        fun genDouble(): Gen<Double> =
            Gen(State { double(it) })

        //tag::init[]
        fun <A> weighted(
            pga: Pair<Gen<A>, Double>,
            pgb: Pair<Gen<A>, Double>
        ): Gen<A> =
            genDouble().flatMap {
                val (ga, wa) = pga
                val (gb, wb) = pgb
                val p = wa / (wa + wb)
                if (it <= p) ga else gb
            }
        //end::init[]
    }

    fun <B> flatMap(f: (A) -> Gen<B>): Gen<B> =
        Gen(sample.flatMap { a -> f(a).sample })
}
