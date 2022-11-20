/**
 * @author Bilal El Uneis
 * @since Oct 2022
 * bilaleluneis@gmail.com
 */

package org.distributed.types

import org.distributed.common.Host
import org.junit.jupiter.api.extension.Extension

object TestsTypePublisher : Extension {

    init{
        println("Publishing Distributed Types")
        RegisteredHost.init(8081, Host())
        RegisteredHost.registry?.apply {
            publish<Bag>(BasicBag())
        }
    }

}