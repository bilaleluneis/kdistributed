/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package common

import java.io.Serializable

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
