/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */
package org.distributed.types

import arrow.core.Either
import arrow.core.Either.Companion.catch
import org.distributed.common.*
import java.net.InetAddress
import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry.createRegistry
import java.rmi.registry.LocateRegistry.getRegistry
import java.rmi.server.UnicastRemoteObject.exportObject

@Throws(RemoteException::class)
inline fun <reified T : Distributed> List<Host>.consume(): List<T> {
    return mapNotNull {
        when (val c = catch { getRegistry(it.ip, it.port).lookup(T::class.simpleName) }) {
            is Either.Left -> {
                println("Host: ${it.name} not availabe due ${c.value.message}")
                null
            }
            is Either.Right -> c.value as T
        }
    }
}

//TODO: is there a way to publish same type to multiple ports without getting Exported Exception ?
@Throws(RemoteException::class)
inline fun <reified T : Distributed> List<Host>.publish(dtype: T) {
    first { it.name == InetAddress.getLocalHost().hostName }.apply {
        System.setProperty("java.rmi.server.hostname", ip)
        createRegistry(port).bind(T::class.simpleName, exportObject(dtype, port))
    }
}








