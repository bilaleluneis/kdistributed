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
    fun <T> create(group: GrpID, values: List<T>) : Bag
}

/**
 * Server (Node) side implementation of the Bag Remote Interface
 */
class BagService : Bag {

    private val dataStore = DataStore()
    private val ops = OperationStore()

    override fun <T> create(group: GrpID, values: List<T>) : BagService {
        values.map { DataWithUuiD(it, UuID()) }.forEach { dataStore[group] = it }
        return this
    }

    override fun <T> filter(grp: GrpID, f: (T) -> Boolean) { ops[grp] = Filter(f) }

    override fun <T, R> map(grp: GrpID, m: (T) -> R) { ops[grp] = Map(m) }

    override fun <T, R> reduce(grp: GrpID, r: (List<T>) -> R): R? {
       val resultList =  ops[grp].fold(dataStore[grp]){
            currResult, op ->  op.eval(currResult)
        }
        ops.delete(grp)
        return Reduce(r).eval(resultList).first().data
    }

}

/**
 * This class is to be used for convenience on client side.
 * it will take care of aquiring all available Bags on registered Nodes
 * and run operations on the cluster.
 */
class BagClient<T>(hosts: List<Host>, chunkSize: Int = 100, vararg values: T) {

    private val availableBags = hosts.consume<Bag>()
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