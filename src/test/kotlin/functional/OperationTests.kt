/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package functional

import common.*
import org.junit.jupiter.api.Test

class OperationTests {

    private val data = mapOf(
        // data containing ints
        "INTS" to Array<Data>(50) { DataWithUuiD(it, UuID(genId(IDType.UUID))) }.toList(),
        // data containing strings
        "STRINGS" to Array<Data>(50) { DataWithUuiD(it.toString(), UuID(genId(IDType.UUID))) }.toList()
    )
    private lateinit var ops: List<FunctionalOps>

    @Test
    fun functionalOpsTest() {
        ops = listOf(
            Filter<Int> { it < 30 },
            Map<Int, Int> { 1 },
            Reduce<Int> { it.sum() }
        )
        var currResult = data["INTS"]!!
        ops.forEach { currResult = it.eval(currResult) }
        assert(currResult.size == 1)
        assert(currResult.toDataOnly<Int>()[0].data == 30)
    }

}






