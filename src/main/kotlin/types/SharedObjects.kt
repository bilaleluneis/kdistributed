/**
 * @author Bilal El Uneis
 * @since Oct 2022
 * bilaleluneis@gmail.com
 */

package types

import common.Data
import common.GrpID
import functional.FunctionalOps

//TODO: change some of the methods to operators and infix

/**
 * Singlton instance of Data Store made available to Distributed types
 * to reduce the need of creating Data Store for each type
 **/
object DataStore {

    private val data = mutableMapOf<GrpID, MutableList<Data>>()

    fun insert(value : Data, forGrp: GrpID) {
        data.getOrPut(forGrp) { mutableListOf() }.add(value)
    }

    fun retrieve(fromGrp: GrpID) = data[fromGrp]


    fun delete(grp: GrpID){ data.remove(grp) }

}

/**
 * Singleton instance that holds Functional Operations
 * to be performed on a Group
 **/
object Operations {

    private val ops = mutableMapOf<GrpID, Array<FunctionalOps>>()

    fun insert(op: FunctionalOps, forGrp: GrpID) {
        ops.getOrPut(forGrp) { arrayOf() } + op
    }

    fun retrieve(forGrp: GrpID) = ops[forGrp]

    fun delete(grp: GrpID) { ops.remove(grp) }

}