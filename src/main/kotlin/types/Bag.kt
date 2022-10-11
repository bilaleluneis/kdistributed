/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package types

import common.*
import functional.*
import java.rmi.RemoteException

interface Bag : Functional {
    @Throws(RemoteException::class)
    fun <T> create(vararg values: T): Pair<Bag, GrpID>
}


class BasicBag : Bag {

    override fun <T> create(vararg values: T): Pair<BasicBag, GrpID> {
        val grp = GrpID()
        values.map { DataWithUuiD(it, UuID()) }.forEach { DataStore.insert(it, grp) }
        return Pair(this, grp)
    }

    override fun <T> filter(grp: GrpID, f: (T) -> Boolean) = Operations.insert(Filter(f), grp)

    override fun <T, R> map(grp: GrpID, m: (T) -> R) = Operations.insert(Map(m), grp)

    override fun <T, R> reduce(grp: GrpID, r: (List<T>) -> R): R? {
        val ops = Operations.retrieve(grp)
        var currEval = DataStore.retrieve(grp).orEmpty()
        return ops?.forEach { op -> currEval = op.eval(currEval) }.run {
            Reduce(r).eval(currEval).toDataOnly<R>().first().data
        }
    }

}