/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */
package org.distributed.types

import org.distributed.common.Distributed
import org.distributed.common.Host
import org.distributed.common.InvalidOpProviderException
import java.net.InetAddress
import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry
import java.rmi.registry.Registry
import java.rmi.server.UnicastRemoteObject

object RegisteredHost{

    private var registry : Registry? = null

    val currentHostName: String = InetAddress.getLocalHost().hostName

    var hostList : Array<Host> = emptyArray()
        private set

    var assignedPort : Int = 0
        private set

    fun init(port: Int, vararg hosts : Host) : Registry {
        if(hostList.isEmpty()){
            hostList = arrayOf(*hosts) // TODO: check if hosts is empty?
            assignedPort = port
            registry = LocateRegistry.createRegistry(port)
        }
        return registry!!
    }

}

@Throws(InvalidOpProviderException::class)
inline fun <reified T : Distributed> consume(): List<T> {
    return RegisteredHost.hostList.map{
        LocateRegistry  .getRegistry(it.ip, RegisteredHost.assignedPort)
                        .lookup(T::class.simpleName) as T
    }.toList()
}

@Throws(RemoteException::class)
inline fun <reified T: Distributed> Registry.publish(dtype: T) {
    RegisteredHost.hostList.firstOrNull{it.name == RegisteredHost.currentHostName}?.apply {
        System.setProperty("java.rmi.server.hostname", ip)
    }
    val remoteObj = UnicastRemoteObject.exportObject(dtype, RegisteredHost.assignedPort)
    bind(T::class.simpleName, remoteObj)
}





