/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package common

import java.io.Serializable
import java.rmi.Remote
import java.rmi.RemoteException

@JvmInline
value class Port(val value: Int = 8081)

@JvmInline
value class Host(val value: String = "localhost")

@JvmInline
value class GrpID(val value: String = genId(IDType.GRPID)) : Serializable

@JvmInline
value class UuID(val value: String = genId(IDType.UUID)) : Serializable

sealed class Data : Serializable
data class DataOnly<T>(val data: T) : Data()
data class DataWithUuiD<T>(val data: T, val uuid: UuID) : Data()

fun <T> List<Data>.toDataOnly(): List<DataOnly<T>> = when (this[0]) {
    is DataOnly<*> -> this.filterIsInstance<DataOnly<T>>()
    is DataWithUuiD<*> -> this.filterIsInstance<DataWithUuiD<T>>().map { DataOnly(it.data) }
}

// all distributed types interfaces will need to implement this
interface DistributedType : Remote

interface Bag : DistributedType {

    @Throws(RemoteException::class)
    fun <T> create(vararg values: T): Pair<Bag, GrpID>

    @Throws(RemoteException::class)
    fun <T> filter(grp: GrpID, f: (T) -> Boolean): Bag

    @Throws(RemoteException::class)
    fun <T, R> map(grp: GrpID, m: (T) -> R): Bag

    @Throws(RemoteException::class)
    fun <T> reduce(grp: GrpID,r: (List<T>) -> T): T

}

fun <T> Pair<Bag, GrpID>.filter(f: (T) -> Boolean) = let{
        (bag, grp) -> bag.filter(grp, f)
        this
}

fun <T, R> Pair<Bag, GrpID>.map(m: (T) -> R) = let{
        (bag, grp) -> bag.map(grp, m)
        this
}

fun <T> Pair<Bag, GrpID>.reduce(r: (List<T>) -> T) = let{
        (bag, grp) -> bag.reduce(grp, r)
}

data class DistNode<T>(
    val data:   T,
    val grpId:  GrpID = GrpID(),
    val uuid:   UuID =  UuID(),
    val parent: UuID =  UuID(),
    val child:  UuID =  UuID(),
) : Serializable