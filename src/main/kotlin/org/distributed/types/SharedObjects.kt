/**
 * @author Bilal El Uneis
 * @since Oct 2022
 * bilaleluneis@gmail.com
 */

package org.distributed.types

import org.distributed.common.Data
import org.distributed.common.GrpID
import org.distributed.functional.FunctionalOps

/**
 * Singlton instance of Data Store made available to Distributed types
 * to reduce the need of creating Data Store for each type
 **/
object DataStore {

    private val data = mutableMapOf<GrpID, MutableList<Data>>()

    operator fun set(key: GrpID, value: Data) = insert(value, key)

    operator fun get(key: GrpID) : List<Data> = retrieve(key)

    private fun insert(value: Data, forGrp: GrpID) {
        data.getOrPut(forGrp) { mutableListOf() }.add(value)
    }

    private fun retrieve(fromGrp: GrpID) = data[fromGrp] ?: emptyList()

    fun delete(grp: GrpID) = data.remove(grp)

}

/**
 * Singleton instance that holds Functional Operations
 * to be performed on a Group
 **/
object Operations {

    private val ops = mutableMapOf<GrpID, ArrayList<FunctionalOps>>()

    operator fun set(key: GrpID, op: FunctionalOps) = insert(op, key)

    operator fun get(key: GrpID) : ArrayList<FunctionalOps> = retrieve(key)

    private fun insert(op: FunctionalOps, forGrp: GrpID) {
        ops.getOrPut(forGrp) { arrayListOf() } += op
    }

    private fun retrieve(forGrp: GrpID) = ops[forGrp] ?: arrayListOf()

    fun delete(grp: GrpID) = ops.remove(grp)

}