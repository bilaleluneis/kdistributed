/**
 * @author Bilal El Uneis
 * @since Oct 2022
 * bilaleluneis@gmail.com
 */

package org.distributed.types

import org.distributed.common.Host
import org.junit.jupiter.api.extension.Extension

internal val Hosts = listOf(
    Host(name = "Bilals-MacBook-Pro.local", port = 8081),
    Host(name = "Bilals-MacBook-Pro.local", port = 8082),
    Host(name = "Bilals-MacBook-Pro.local", port = 8083),
    Host(name = "Bilals-MacBook-Pro.local", port = 8084),
)

internal object TestsTypePublisher : Extension {

    init{
        println("Publishing Distributed Types")
        Hosts.publish<Bag>(BagService::class)
    }

}