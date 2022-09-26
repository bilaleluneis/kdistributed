/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package types

import common.DistributedType
import common.GrpID
import java.rmi.RemoteException

interface Bag : DistributedType {

    @Throws(RemoteException::class)
    fun <T> create(vararg values: T): Pair<Bag, GrpID>

    @Throws(RemoteException::class)
    fun <T> filter(grp: GrpID, f: (T) -> Boolean): Bag

    @Throws(RemoteException::class)
    fun <T, R> map(grp: GrpID, m: (T) -> R): Bag

    @Throws(RemoteException::class)
    fun <T> reduce(grp: GrpID, r: (List<T>) -> T): T

}

fun <T> Pair<Bag, GrpID>.filter(f: (T) -> Boolean) = let {
        (bag, grp) -> bag.filter(grp, f)
        this
}

fun <T, R> Pair<Bag, GrpID>.map(m: (T) -> R) = let {
        (bag, grp) -> bag.map(grp, m)
        this
}

fun <T> Pair<Bag, GrpID>.reduce(r: (List<T>) -> T) = let {
        (bag, grp) -> bag.reduce(grp, r)
}