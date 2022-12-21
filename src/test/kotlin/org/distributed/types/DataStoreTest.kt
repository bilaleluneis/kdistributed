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
        val dataStore = DataStore()
        assert(dataStore[grp].isEmpty()) { "group ID already exist" }
        val data = listOf(1, 2, 3, 4, 5)
        data.toData().forEach { dataStore[grp] = it }
        assert(dataStore[grp].isNotEmpty()) { "insertion into DataStore failed" }
        assert(dataStore[grp].size == data.size) { "DataStore size mismatch" }
        assert(dataStore[grp].filterIsInstance<DataOnly<Int>>().all { it.data in data}) { "Data mismatch" }
        dataStore.delete(grp)
        assert(dataStore[grp].isEmpty()) { "failed to delete DataStore for group: $grp" }

    }

}