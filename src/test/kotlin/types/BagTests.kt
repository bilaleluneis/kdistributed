/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package types

import common.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestsTypePublisher::class)
class BagTests {

    @Test
    fun basicBagTest() {
        val bag = consume<Bag>(Host("localhost"), Port(8081))
        val result = bag.create(1, 2, 3, 4, 5).filter<Int> {it < 3}
                                                   .map<Int, Int>{ it + 0 }
                                                   .reduce<Int, Int>{it.first()}
        assert(result == 1)
    }

}





