package chapter8.exercises.ex3

//tag::init[]
interface Prop {
    fun check(): Boolean
    fun and(p: Prop): Prop {
        val result = check() && p.check()
        return object: Prop {
            override fun check() = result
        }
    }
}
//end::init[]
