/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */
package types

import common.Distributed
import common.Host
import common.InvalidOpProviderException
import common.Port
import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry
import java.rmi.registry.Registry
import java.rmi.server.UnicastRemoteObject

@Throws(InvalidOpProviderException::class)
inline fun <reified T : Distributed> consume(host: Host, port: Port): T {
    return LocateRegistry.getRegistry(host.value, port.value).lookup(T::class.simpleName) as T
}

@Throws(RemoteException::class)
inline fun <reified T: Distributed> Registry.publish(dtype: T) {
    val remoteObj = UnicastRemoteObject.exportObject(dtype, 0)
    bind(T::class.simpleName, remoteObj)
}




