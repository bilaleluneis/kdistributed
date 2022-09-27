/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

import common.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import types.*

class WorkerTests {

    @Test
    fun workerServImplTest() {
        val bag = Worker.consume<Bag>(Host("localhost"), Port(8081))
        val result = bag.create(1, 2, 3, 4, 5).filter<Int> {it < 3}.map<Int, Int>{ it + 0 }.reduce<Int, Int>{it.first()}
        assert(result == 1)
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun startWorker() {
            Worker.publish<Bag>(Port(8081), BasicBag())
        }
    }

}





