/**
 * @author Bilal El Uneis
 * @since Nov 2022
 * bilaleluneis@gmail.com
 */

package org.distributed.types

import org.distributed.common.GrpID
import org.distributed.functional.Filter
import org.distributed.functional.Map
import org.distributed.functional.Reduce
import org.junit.jupiter.api.Test

internal class OperationsTest{

    @Test
    fun operations() {

        val grp = GrpID()
        val operations = OperationStore()
        val ops = arrayOf(
            Filter<Int>{ it < 10 },
            Map<Int, Int>{ it * it },
            Reduce<Int, Int> {it.size}
        )

        assert(operations[grp].isEmpty()) { "GrpID already exist" }
        ops.forEach { operations[grp] = it }
        assert(operations[grp].isNotEmpty()) { "Ops count should not be zero" }
        assert(operations[grp].size == ops.size) { "ops stores != ops inserted" }
        operations.delete(grp)
        assert(operations[grp].isEmpty()) { "operations for groupID: $grp were not removed" }

    }

}

