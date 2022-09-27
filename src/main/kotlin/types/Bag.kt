/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package types

import common.*
import functional.Filter
import functional.FunctionalOps
import functional.Map
import functional.Reduce
import java.rmi.RemoteException

interface Bag : Functional {
    @Throws(RemoteException::class)
    fun <T> create(vararg values: T): Pair<Bag, GrpID>
}


class BasicBag : Bag {

    private var data    = mutableMapOf<GrpID, List<Data>>()
    private var ops     = mutableMapOf<GrpID, MutableList<FunctionalOps>>()

    override fun <T> create(vararg values: T): Pair<BasicBag, GrpID> {
        val grp = GrpID()
        data[grp] = values.map { DataWithUuiD(it, UuID()) }
        return Pair(this, grp)
    }

    override fun <T> filter(grp: GrpID, f: (T) -> Boolean): BasicBag {
        ops.getOrPut(grp) { mutableListOf() }.add(Filter(f))
        return this
    }

    override fun <T, R> map(grp: GrpID, m: (T) -> R): BasicBag {
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