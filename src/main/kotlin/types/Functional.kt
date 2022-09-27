/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package types

import common.Distributed
import common.GrpID
import java.rmi.RemoteException

interface Functional : Distributed {
    @Throws(RemoteException::class)
    fun <T> filter(grp: GrpID, f: (T) -> Boolean): Functional

    @Throws(RemoteException::class)
    fun <T, R> map(grp: GrpID, m: (T) -> R): Functional

    @Throws(RemoteException::class)
    fun <T> reduce(grp: GrpID, r: (List<T>) -> T): T
}

fun <T> Pair<Functional, GrpID>.filter(f: (T) -> Boolean) = let {
        (fType, grp) -> fType.filter(grp, f)
        this
}

fun <T, R> Pair<Functional, GrpID>.map(m: (T) -> R) = let {
        (fType, grp) -> fType.map(grp, m)
        this
}

fun <T> Pair<Functional, GrpID>.reduce(r: (List<T>) -> T) = let {
        (fType, grp) -> fType.reduce(grp, r)
}