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

interface BagI : Functional {
    @Throws(RemoteException::class)
    fun <T> create(group: GrpID, values: List<T>) : BagI
}

internal class BagImpl : BagI {

    override fun <T> create(group: GrpID, values: List<T>) : BagImpl {
        //TODO: change to map{}.tolist() or collect instead of looping as group is same
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

class Bag<T>(hosts: List<Host>, chunkSize: Int = 100, vararg values: T) {

    private val availableBags = hosts.consume<BagI>()
    private val group = GrpID()

    init {
        values.toList().chunked(chunkSize / availableBags.size).forEach {
            availableBags.shuffled().first().create(group, it)
        }
    }

    fun filter(f: (T) -> Boolean) { availableBags.forEach { it.filter(group, f) } }

    fun <I, R> map(m: (I) -> R) { availableBags.forEach { it.map(group, m) } }

    fun <I, R> reduce(r: (List<I>) -> R): List<R> = availableBags.mapNotNull { it.reduce(group, r) }

}