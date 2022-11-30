/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */
package org.distributed.types

import org.distributed.common.Distributed
import org.distributed.common.Host
import java.net.InetAddress
import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry.createRegistry
import java.rmi.registry.LocateRegistry.getRegistry
import java.rmi.server.UnicastRemoteObject.exportObject


@Throws(RemoteException::class)
inline fun <reified T : Distributed> List<Host>.consume(): List<T> {
    return map{ getRegistry(it.ip, it.port).lookup(T::class.simpleName) as T }.toList()
}

//TODO: is there a way to publish same type to multiple ports without getting Exported Exception ?
@Throws(RemoteException::class)
inline fun <reified T: Distributed> List<Host>.publish(dtype: T) {
    first { it.name == InetAddress.getLocalHost().hostName }.apply {
        System.setProperty("java.rmi.server.hostname", ip)
        createRegistry(port).bind(T::class.simpleName, exportObject(dtype, port))
    }
}





