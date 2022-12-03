/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package org.distributed.types

import org.distributed.common.GrpID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(TestsTypePublisher::class)
internal class BagTests {

    @Test
    fun bagImplTest() {

        val bags = Hosts.consume<BagI>()
        val grp = GrpID()
        val bag = bags.first().create(grp, listOf(1, 2, 3))
        bag.filter<Int>(grp) { it < 2 }
        bag.map<Int, Int>(grp) { it }
        val result = bag.reduce<Int, Int>(grp) { it.size } ?: 0
        assert(result == 1) { "failed" }
        assert(Operations[grp].isEmpty()) { "ops were not removed after reduction" }

    }

    @Test
    fun bagTest() {
        val values = (1..100).toList().toTypedArray()
        val bag = Bag(hosts = Hosts, values = values)
        bag.filter { it < 25 }
        bag.map<Int, Int> { it }
        assert(bag.reduce<Int, Int>{ it.size }.first() == 24) { "bagClient Test Failed" }
    }

    /**
     * bellow logic will be used as detail impl for splitting
     * data in BagClient() accross available bags
     */
    @Test
    fun dataSplitForBagsLogic() {
        val numOfBags = 2
        val chunkSize = 50
        val result =  (1..10).toList().chunked(chunkSize / numOfBags)
        assert(result.size == 1)
    }

    //TODO: test when calling create on a bag multiple times with chunks of values
    // that values are appended into the list associated with grp id and not
    // ends up creating new list under that group

}





