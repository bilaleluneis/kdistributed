/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

import common.*
import functional.Filter
import functional.FunctionalOps
import functional.Map
import functional.Reduce
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import types.Bag
import types.filter
import types.map
import types.reduce

class WorkerTests {

    @Test
    fun workerServImplTest() {
        val bag = Worker.consume<Bag>(Host("localhost"), Port(8081))
        val result = bag.create(1, 2, 3, 4, 5).filter<Int> {it < 3}.map<Int, Int>{ it + 0 }.reduce<Int>{it.first()}
        assert(result == 1)
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun startWorker() {
            Worker.publish<Bag>(Port(8081), TestBagImpl())
        }
    }

}

class TestBagImpl : Bag {

    private var data    = mutableMapOf<GrpID, List<Data>>()
    private var ops     = mutableMapOf<GrpID, MutableList<FunctionalOps>>()

    override fun <T> create(vararg values: T): Pair<TestBagImpl, GrpID> {
        val grp = GrpID()
        data[grp] = values.map { DataWithUuiD(it, UuID()) }
        return Pair(this, grp)
    }

    override fun <T> filter(grp: GrpID, f: (T) -> Boolean): TestBagImpl {
        ops.getOrPut(grp) { mutableListOf() }.add(Filter(f))
        return this
    }

    override fun <T, R> map(grp: GrpID, m: (T) -> R): TestBagImpl {
        ops.getOrPut(grp) { mutableListOf() }.add(Map(m))
        return this
    }

    override fun <T> reduce(grp: GrpID, r: (List<T>) -> T): T {
        require(grp in ops)
        val result = ops[grp]?.run {
            this.add(Reduce(r))
            var currEval = data[grp]!!
            this.forEach { currEval = it.eval(currEval) }
            currEval.toDataOnly<T>().first().data
        }
        return result!!
    }

}





