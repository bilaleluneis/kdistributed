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
import java.rmi.registry.LocateRegistry.createRegistry
import java.rmi.registry.LocateRegistry.getRegistry
import java.rmi.registry.Registry
import java.rmi.server.UnicastRemoteObject

object RegisteredHost{

    var registry : Registry? = null
        private set
        get(){
            if(field == null && assignedPort != 0){
                field =  createRegistry(assignedPort)
            }
            return field
        }

    val currentHostName: String = InetAddress.getLocalHost().hostName

    var hostList : Array<Host> = emptyArray()
        private set

    var assignedPort : Int = 0
        private set

    fun init(port: Int, vararg hosts : Host) {
        if(hostList.isEmpty()){
            hostList = arrayOf(*hosts)
            assignedPort = port
        }
    }

}

@Throws(InvalidOpProviderException::class)
inline fun <reified T : Distributed> consume(): List<T> {
    return RegisteredHost.hostList.map{
        getRegistry(it.ip, RegisteredHost.assignedPort).lookup(T::class.simpleName) as T
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





