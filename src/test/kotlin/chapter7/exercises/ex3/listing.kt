package chapter7.exercises.ex3

import chapter7.sec3.Par
import chapter7.sec3.Pars
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

data class TimedMap2Future<A, B, C>(
    val pa: Future<A>,
    val pb: Future<B>,
    val f: (A, B) -> C
) : Future<C> {

    override fun isDone(): Boolean = pa.isDone && pb.isDone

    override fun get(): C = f(pa.get(), pb.get())

    override fun get(to: Long, tu: TimeUnit): C {
        val start = System.currentTimeMillis()
        val a = pa.get(to, tu)
        val r = TimeUnit.MILLISECONDS.convert(to, tu) - System.currentTimeMillis() + start
        return f(a, pb.get(r, TimeUnit.MILLISECONDS))
    }

    override fun cancel(b: Boolean): Boolean = pa.cancel(b) && pb.cancel(b)

    override fun isCancelled(): Boolean = pa.isCancelled && pb.isCancelled
}

fun <A, B, C> map2(
    a: Par<A>,
    b: Par<B>,
    f: (A, B) -> C
): Par<C> = { es: ExecutorService ->
    val af = a(es)
    val bf = b(es)
    TimedMap2Future(af, bf, f)
}

class Exercise3 : WordSpec({

    val es: ExecutorService =
        ThreadPoolExecutor(
            1, 1, 5, TimeUnit.SECONDS, LinkedBlockingQueue()
        )

    "map2" should {
        "allow two futures to run within a given timeout" {

            val pa = Pars.fork {
                Thread.sleep(400L)
                Pars.unit(1)
            }
            val pb = Pars.fork {
                Thread.sleep(500L)
                Pars.unit("1")
            }
            val pc: Par<Long> =
                map2(pa, pb) { a: Int, b: String ->
                    a + b.toLong()
                }

            withContext(Dispatchers.IO) {
                pc(es).get(1, TimeUnit.SECONDS) shouldBe 2L
            }
        }

        "timeout if first future exceeds timeout" {

            val pa = Pars.fork {
                Thread.sleep(1100L)
                Pars.unit(1)
            }
            val pb = Pars.fork {
                Thread.sleep(500L)
                Pars.unit("1")
            }
            val pc: Par<Long> =
                map2(pa, pb) { a: Int, b: String ->
                    a + b.toLong()
                }

            withContext(Dispatchers.IO) {
                shouldThrow<TimeoutException> {
                    pc(es).get(1, TimeUnit.SECONDS)
                }
            }
        }

        "timeout if second future exceeds timeout" {

            val pa = Pars.fork {
                Thread.sleep(100L)
                Pars.unit(1)
            }
            val pb = Pars.fork {
                Thread.sleep(1000L)
                Pars.unit("1")
            }
            val pc: Par<Long> =
                map2(pa, pb) { a: Int, b: String ->
                    a + b.toLong()
                }

            withContext(Dispatchers.IO) {
                shouldThrow<TimeoutException> {
                    pc(es).get(1, TimeUnit.SECONDS)
                }
            }
        }
    }
})
