/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package types

import common.*
import functional.filter
import functional.map
import functional.reduce
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestsTypePublisher::class)
class BagTests {

    @Test
    fun basicBagTest() {
        val bags = consume<Bag>(Port(), listOf(Host()))
        val result = bags.first().create(1, 2, 3, 4, 5).apply {
            filter<Int> {it < 3}
            map<Int, Int>{ it + 0 }
        }.reduce<Int, Int> { it.first() }
        assert(result == 1)
    }

}





