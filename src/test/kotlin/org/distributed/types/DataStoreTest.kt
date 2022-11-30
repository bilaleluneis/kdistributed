/**
 * @author Bilal El Uneis
 * @since Nov 2022
 * bilaleluneis@gmail.com
 */

package org.distributed.types

import org.distributed.common.*
import org.junit.jupiter.api.Test

internal class DataStoreTest {

    @Test
    fun dataStorage() {

        val grp = GrpID()
        assert(DataStore[grp].isEmpty()) { "group ID already exist" }
        val data = listOf(1, 2, 3, 4, 5)
        data.toData().forEach { DataStore[grp] = it }
        assert(DataStore[grp].isNotEmpty()) { "insertion into DataStore failed" }
        assert(DataStore[grp].size == data.size) { "DataStore size mismatch" }
        assert(DataStore[grp].filterIsInstance<DataOnly<Int>>().all { it.data in data}) { "Data mismatch" }
        DataStore.delete(grp)
        assert(DataStore[grp].isEmpty()) { "failed to delete DataStore for group: $grp" }

    }

}