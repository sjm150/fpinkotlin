package chapter6.exercises.ex11

import arrow.core.Id
import arrow.core.Tuple2
import arrow.core.extensions.id.monad.monad
import arrow.mtl.State
import arrow.mtl.StateApi.get
import arrow.mtl.StateApi.modify
import arrow.mtl.extensions.fx
import arrow.mtl.runS
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import kotlin.collections.listOf

//tag::init1[]
sealed class Input

object Coin : Input()
object Turn : Input()

data class Machine(
    val locked: Boolean,
    val candies: Int,
    val coins: Int
)
//end::init1[]

class Exercise11 : WordSpec({

    //tag::init2[]
    fun simulateMachine(
        inputs: List<Input>
    ): State<Machine, Tuple2<Int, Int>> =
        State.fx(Id.monad()) {
            inputs.forEach { input ->
                modify<Machine> op@{
                    if (it.candies == 0) return@op it
                    when (input) {
                        is Coin -> Machine(false, it.candies, it.coins + if (it.locked) 1 else 0)
                        is Turn -> Machine(true, it.candies - if (it.locked) 0 else 1, it.coins)
                    }
                }.bind()
            }

            get<Machine>().bind().let { Tuple2(it.candies, it.coins) }
        }
    //end::init2[]

    "simulateMachine" should {
        "allow the purchase of a single candy" {
            val actions = listOf(Coin)
            val before =
                Machine(locked = true, candies = 1, coins = 0)
            val after =
                Machine(locked = false, candies = 1, coins = 1)
            simulateMachine(actions).runS(before) shouldBe after
        }
        "allow the redemption of a single candy" {
            val actions = listOf(Turn)
            val before =
                Machine(locked = false, candies = 1, coins = 1)
            val after = Machine(locked = true, candies = 0, coins = 1)
            simulateMachine(actions).runS(before) shouldBe after
        }
        "allow purchase and redemption of a candy" {
            val actions = listOf(Coin, Turn)
            val before =
                Machine(locked = true, candies = 1, coins = 0)
            val after = Machine(locked = true, candies = 0, coins = 1)
            simulateMachine(actions).runS(before) shouldBe after
        }
    }

    "inserting a coin into a locked machine" should {
        "unlock the machine if there is some candy" {
            val actions = listOf(Coin)
            val before =
                Machine(locked = true, candies = 1, coins = 0)
            val after =
                Machine(locked = false, candies = 1, coins = 1)
            simulateMachine(actions).runS(before) shouldBe after
        }
    }
    "inserting a coin into an unlocked machine" should {
        "do nothing" {
            val actions = listOf(Coin)
            val before =
                Machine(locked = false, candies = 1, coins = 1)
            val after =
                Machine(locked = false, candies = 1, coins = 1)
            simulateMachine(actions).runS(before) shouldBe after
        }
    }
    "turning the knob on an unlocked machine" should {
        "cause it to dispense candy and lock" {
            val actions = listOf(Turn)
            val before =
                Machine(locked = false, candies = 1, coins = 1)
            val after = Machine(locked = true, candies = 0, coins = 1)
            simulateMachine(actions).runS(before) shouldBe after
        }
    }
    "turning the knob on a locked machine" should {
        "do nothing" {
            val actions = listOf(Turn)
            val before =
                Machine(locked = true, candies = 1, coins = 1)
            val after = Machine(locked = true, candies = 1, coins = 1)
            simulateMachine(actions).runS(before) shouldBe after
        }
    }
    "a machine that is out of candy" should {
        "ignore the turn of a knob" {
            val actions = listOf(Turn)
            val before =
                Machine(locked = true, candies = 0, coins = 0)
            val after = Machine(locked = true, candies = 0, coins = 0)
            simulateMachine(actions).runS(before) shouldBe after
        }
        "ignore the insertion of a coin" {
            val actions = listOf(Coin)
            val before =
                Machine(locked = true, candies = 0, coins = 0)
            val after = Machine(locked = true, candies = 0, coins = 0)
            simulateMachine(actions).runS(before) shouldBe after
        }
    }
})
