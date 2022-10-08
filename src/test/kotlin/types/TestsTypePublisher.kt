package types

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource
import java.rmi.registry.LocateRegistry

object TestsTypePublisher : BeforeAllCallback, CloseableResource {

    init{
        println("Publishing Distributed Types")
        LocateRegistry.createRegistry(8081)?.apply{publish<Bag>(BasicBag()) }
    }

    override fun beforeAll(context: ExtensionContext?) {}

    override fun close() {}

}