/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */
package org.distributed.types

import arrow.core.Either.Companion.catch
import org.distributed.common.*
import java.net.InetAddress
import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry.createRegistry
import java.rmi.registry.LocateRegistry.getRegistry
import java.rmi.server.UnicastRemoteObject.exportObject
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

// TODO: rewrite this , looks way too complicated!!
@Throws(RemoteException::class)
inline fun <reified T : Distributed> List<Host>.consume(): List<T> {
    val type = T::class.simpleName
    return mapNotNull { host -> catch {
            getRegistry(host.ip, host.port)
        }.fold( { null }, // case of exception just return null
                // got a registry then list all matching keys
                { it.list().filter { key -> "$type-" in key }.map {k -> Pair(it, k) } })
    }.flatten().map{ (reg, tName) -> reg.lookup(tName) as T}
}

@Throws(RemoteException::class)
inline fun <reified T : Distributed> List<Host>.publish(dtype: KClass<out T>) {
    first { it.name == InetAddress.getLocalHost().hostName }.apply {
        System.setProperty("java.rmi.server.hostname", ip)
    }
    forEach { host ->
        val name = T::class.simpleName + "-${genId(IDType.TYPEID)}"
        createRegistry(host.port).bind(name, exportObject(dtype.createInstance(), host.port))
    }
}
