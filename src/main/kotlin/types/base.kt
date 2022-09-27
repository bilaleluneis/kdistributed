/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package types

import common.GrpID
import java.rmi.Remote
import java.rmi.RemoteException

interface Functional : Distributed {
    @Throws(RemoteException::class)
    fun <T> filter(grp: GrpID, f: (T) -> Boolean): Functional

    @Throws(RemoteException::class)
    fun <T, R> map(grp: GrpID, m: (T) -> R): Functional

    @Throws(RemoteException::class)
    fun <T, R> reduce(grp: GrpID, r: (List<T>) -> R): R
}

fun <T> Pair<Functional, GrpID>.filter(f: (T) -> Boolean) = let {
        (fType, grp) -> fType.filter(grp, f)
        this
}

fun <T, R> Pair<Functional, GrpID>.map(m: (T) -> R) = let {
        (fType, grp) -> fType.map(grp, m)
        this
}

fun <T, R> Pair<Functional, GrpID>.reduce(r: (List<T>) -> R) = let {
        (fType, grp) -> fType.reduce(grp, r)
}

// all distributed types interfaces will need to implement this
interface Distributed : Remote