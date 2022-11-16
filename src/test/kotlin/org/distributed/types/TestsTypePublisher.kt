/**
 * @author Bilal El Uneis
 * @since Oct 2022
 * bilaleluneis@gmail.com
 */

package org.distributed.types

import org.junit.jupiter.api.extension.Extension
import java.rmi.registry.LocateRegistry

object TestsTypePublisher : Extension {

    init{
        println("Publishing Distributed Types")
        LocateRegistry.createRegistry(8081)?.apply{
            publish<Bag>(BasicBag())
        }
    }

}