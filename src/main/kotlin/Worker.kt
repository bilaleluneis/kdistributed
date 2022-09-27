/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

import types.Distributed
import common.Host
import common.InvalidOpProviderException
import common.Port
import java.rmi.registry.LocateRegistry
import java.rmi.server.UnicastRemoteObject

class Worker private constructor() {
    companion object {

        // used on client to make distributed calls on workers (Distributed Types)
        @Throws(InvalidOpProviderException::class)
        inline fun <reified T : Distributed> consume(host: Host, port: Port): T {
            return LocateRegistry.getRegistry(host.value, port.value).lookup(T::class.simpleName) as T
        }

        // used to expose Distributed Type to network
        @Throws(InvalidOpProviderException::class)
        inline fun <reified T : Distributed> publish(port: Port, dtype: T) {
            val remoteObj = UnicastRemoteObject.exportObject(dtype, 0)
            LocateRegistry.createRegistry(port.value).bind(T::class.simpleName, remoteObj)
        }

    }
}



