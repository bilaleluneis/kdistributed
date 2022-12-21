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

//TODO:
// - write Test to simulate a node down [create your own hosts containing Hosts and a node that doesnt exist].

@ExtendWith(TestsTypePublisher::class)
internal class BagTests {

    @Test
    fun bagImplTest() {

        val bags = Hosts.consume<Bag>()
        val grp = GrpID()
        val bag = bags.first().create(grp, listOf(1, 2, 3))
        bag.filter<Int>(grp) { it < 2 }
        bag.map<Int, Int>(grp) { it }
        val result = bag.reduce<Int, Int>(grp) { it.size } ?: 0
        assert(result == 1) { "failed" }
        //TODO: how to confirm operations were cleared
        //assert(Operations[grp].isEmpty()) { "ops were not removed after reduction" }

    }

    @Test
    fun bagTest() {
        val values = (1..100).toList().toTypedArray()
        val bag = BagClient(hosts = Hosts, values = values, chunkSize = 10)
        bag.filter { it < 25 }
        bag.map<Int, Int> { it }

        // initial reduction happens on each bag and comes back as list
        // containing the reduction result from each bag
        val bagsReduce = bag.reduce<Int, Int>{ it.size }

        // final reduction always happens on client, and takes
        // the reduction result from each bag and applies a client
        // side reduction.
        val finalReduction = bagsReduce.sum()

        assert(finalReduction == 24) { "bagClient Test Failed" }
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

}





