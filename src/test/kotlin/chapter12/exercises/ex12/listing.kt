package chapter12.exercises.ex12

import arrow.Kind
import chapter10.ForOption
import chapter10.None
import chapter10.Some
import chapter10.fix
import chapter12.Applicative
import chapter12.Cons
import chapter12.ForList
import chapter12.ForTree
import chapter12.List
import chapter12.Traversable
import chapter12.Tree
import chapter12.fix

//tag::init1[]
fun <A> optionTraversable(): Traversable<ForOption> = object : Traversable<ForOption> {
    override fun <G, A, B> traverse(
        fa: Kind<ForOption, A>,
        AG: Applicative<G>,
        f: (A) -> Kind<G, B>
    ): Kind<G, Kind<ForOption, B>> =
        when (val oa = fa.fix()) {
            is Some -> AG.map(f(oa.get)) { Some(it) }
            is None -> AG.unit(None)
        }
}
//end::init1[]

//tag::init2[]
fun <A> listTraversable(): Traversable<ForList> = object : Traversable<ForList> {
    override fun <G, A, B> traverse(
        fa: Kind<ForList, A>,
        AG: Applicative<G>,
        f: (A) -> Kind<G, B>
    ): Kind<G, Kind<ForList, B>> =
        fa.fix().foldLeft(AG.unit(List.of())) { glb, a ->
            AG.map2(glb, f(a)) { lb, b -> Cons(b, lb.fix()) }
        }
}
//end::init2[]

//tag::init3[]
fun <A> treeTraversable(): Traversable<ForTree> = object : Traversable<ForTree> {
    override fun <G, A, B> traverse(
        fa: Kind<ForTree, A>,
        AG: Applicative<G>,
        f: (A) -> Kind<G, B>
    ): Kind<G, Kind<ForTree, B>> {
        val ta = fa.fix()
        return AG.map2(
            f(ta.head),
            listTraversable<A>().traverse(ta.tail, AG) { traverse(it, AG, f) }
        ) { b, lfb ->
            Tree(b, lfb.fix().map { it.fix() })
        }
    }
}
//end::init3[]