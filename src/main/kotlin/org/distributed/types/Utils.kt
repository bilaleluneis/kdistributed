/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */
package org.distributed.types

import org.distributed.common.Distributed
import org.distributed.common.Host
import org.distributed.common.InvalidOpProviderException
import org.distributed.common.Port
import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry
import java.rmi.registry.Registry
import java.rmi.server.UnicastRemoteObject

@Throws(InvalidOpProviderException::class)
inline fun <reified T : Distributed> consume(port: Port, hosts: List<Host>): List<T> {
    return hosts.map{
        LocateRegistry  .getRegistry(it.value, port.value)
                        .lookup(T::class.simpleName) as T
    }.toList()
}

@Throws(RemoteException::class)
inline fun <reified T: Distributed> Registry.publish(dtype: T) {
    val remoteObj = UnicastRemoteObject.exportObject(dtype, 0)
    bind(T::class.simpleName, remoteObj)
}




