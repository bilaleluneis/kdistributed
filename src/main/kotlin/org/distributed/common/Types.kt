/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package org.distributed.common

import java.io.Serializable
import java.rmi.Remote

@JvmInline
value class Port(val value: Int = 8081)

@JvmInline
value class Host(val value: String = "0.0.0.0")

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
interface Distributed : Remote

