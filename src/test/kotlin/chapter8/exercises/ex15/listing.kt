package chapter8.exercises.ex15

import chapter7.sec3.Pars
import chapter7.sec4.Par
import chapter7.sec4.unit
import chapter8.Gen
import chapter8.sec4_12.combine

fun pint2(): Gen<Par<Int>> =
    (Gen.choose(0, 10) combine Gen.choose(0, 10))
        .map { (a, b) ->
            Pars.map2(unit(a), unit(b)) { x, y -> x + y }
        }
