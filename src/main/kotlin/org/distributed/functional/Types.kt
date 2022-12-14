/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package org.distributed.functional

import org.distributed.common.*
import java.rmi.RemoteException


sealed class FunctionalOps {
    abstract fun eval(input: List<Data>): List<Data>
}

class Filter<T>(val f: (T) -> Boolean) : FunctionalOps() {
    override fun eval(input: List<Data>) = input.toDataOnly<T>().filter { f(it.data) }
}

class Map<T, R>(val m: (T) -> R) : FunctionalOps() {
    override fun eval(input: List<Data>) = input.toDataOnly<T>().map { DataOnly(m(it.data)) }
}

class Reduce<T, R>(val r: (List<T>) -> R) : FunctionalOps() {
    override fun eval(input: List<Data>): List<DataOnly<R>> {
        val rInput = input.toDataOnly<T>().map { it.data }
        val result = r(rInput)
        return listOf(DataOnly(result))
    }
}

interface Functional : Distributed {
    @Throws(RemoteException::class)
    fun <T> filter(grp: GrpID, f: (T) -> Boolean)

    @Throws(RemoteException::class)
    fun <T, R> map(grp: GrpID, m: (T) -> R)

    @Throws(RemoteException::class)
    fun <T, R> reduce(grp: GrpID, r: (List<T>) -> R): R?
}

fun <T> Pair<Functional, GrpID>.filter(f: (T) -> Boolean) = let {
        (fType, grp) -> fType.filter(grp, f)
}

fun <T, R> Pair<Functional, GrpID>.map(m: (T) -> R) = let {
        (fType, grp) -> fType.map(grp, m)
}

fun <T, R> Pair<Functional, GrpID>.reduce(r: (List<T>) -> R) = let {
        (fType, grp) -> fType.reduce(grp, r)
}