/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package org.distributed.types

import org.distributed.common.DataWithUuiD
import org.distributed.common.GrpID
import org.distributed.common.Host
import org.distributed.common.UuID
import org.distributed.functional.Filter
import org.distributed.functional.Functional
import org.distributed.functional.Map
import org.distributed.functional.Reduce
import java.rmi.RemoteException

interface Bag : Functional {
    @Throws(RemoteException::class)
    fun <T> create(group: GrpID, vararg values: T) : Bag
}


class BasicBag : Bag {

    override fun <T> create(group: GrpID, vararg values: T) : BasicBag {
        values.map { DataWithUuiD(it, UuID()) }.forEach { DataStore[group] = it }
        return this
    }

    override fun <T> filter(grp: GrpID, f: (T) -> Boolean) { Operations[grp] = Filter(f) }

    override fun <T, R> map(grp: GrpID, m: (T) -> R) { Operations[grp] = Map(m) }

    override fun <T, R> reduce(grp: GrpID, r: (List<T>) -> R): R? {
       val resultList =  Operations[grp].fold(DataStore[grp]){
            currResult, op ->  op.eval(currResult)
        }
        Operations.delete(grp)
        return Reduce(r).eval(resultList).first().data
    }

}

class BagClient<T>(hosts: List<Host>, vararg values: T) {

    private val availableBags = hosts.consume<Bag>()
    private val group = GrpID()

    init {
        values.asList().chunked(values.size / availableBags.size).forEach {
            availableBags.shuffled().first().create(group, it)
        }
    }

    fun filter(f: (T) -> Boolean) { availableBags.forEach { it.filter(group, f) } }

    fun <I, R> map(m: (I) -> R) { availableBags.forEach { it.map(group, m) } }

    fun <R> reduce(r: (List<Any>) -> R) : R  {
        val interm = availableBags.mapNotNull { it.reduce(group, r) }
        return r(interm)
    }

}