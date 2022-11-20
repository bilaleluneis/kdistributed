/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package org.distributed.types

import org.distributed.functional.filter
import org.distributed.functional.map
import org.distributed.functional.reduce
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestsTypePublisher::class)
class BagTests {

    @Test
    fun basicBagTest() {
        val bags = consume<Bag>()
        val result = bags.first().create(1, 2, 3, 4, 5).apply {
            filter<Int> {it < 3}
            map<Int, Int>{ it + 0 }
        }.reduce<Int, Int> { it.first() }
        assert(result == 1)
    }

}





