/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package org.distributed.common

import java.io.Serializable
import java.rmi.Remote

data class Host(val name: String    = "localhost",
                val ip: String      = "127.0.0.1",
                val port: Int       = 8081)

@JvmInline
value class GrpID(val value: String = genId(IDType.GRPID)) : Serializable

@JvmInline
value class UuID(val value: String = genId(IDType.UUID)) : Serializable

sealed class Data : Serializable
data class DataOnly<T>(val data: T) : Data()
data class DataWithUuiD<T>(val data: T, val uuid: UuID) : Data()

fun <T> List<Data>.toDataOnly(): List<DataOnly<T>> {
    if (isEmpty()) return emptyList()
    return when (this[0]) {
        is DataOnly<*> -> this.filterIsInstance<DataOnly<T>>()
        is DataWithUuiD<*> -> this.filterIsInstance<DataWithUuiD<T>>().map { DataOnly(it.data) }
    }
}

fun <T> List<T>.toData(): List<Data> = map { DataOnly(it) }

// all distributed types interfaces will need to implement this
interface Distributed : Remote





