/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package org.distributed.types

import org.distributed.common.GrpID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Ignore

@ExtendWith(TestsTypePublisher::class)
internal class BagTests {

    @Test
    fun basicBagTest() {

        val bags = Hosts.consume<Bag>()
        val grp = GrpID()
        val result = bags.first().create(grp, 1, 2, 3).apply {
            filter<Int>(grp){it < 2}
            map<Int, Int>(grp) { it }
        }.reduce<Int, String>(grp) {it.first().toString()}
        assert(result!! == "1") { "failed" }
        assert(Operations[grp].isEmpty()) { "ops were not removed after reduction" }

    }

    @Test
    @Ignore
    fun bagClientTest() {

        BagClient(Hosts, 1, 2, 3).apply{
            filter{ it < 2}
            map<Int, Int>{it}
        }.reduce{ it.first() as Int }

        print("")
    }

}





