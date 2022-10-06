/**
 * @author Bilal El Uneis
 * @since Oct 2022
 * bilaleluneis@gmail.com
 */

package types

import common.GrpID
import common.UuID

data class NodeInfo <T> (
    val data    : T,
    val uuid    : UuID,
    val grpId   : GrpID,
    var parentID: UuID,
    var childID : UuID,
)

interface Node: Distributed {

    fun <T> create(withValue: T) : Pair<UuID, GrpID>
    fun <T> get(id: Pair<UuID, GrpID>) : NodeInfo<T>
    fun <T> update(n: NodeInfo<T>)
    fun delete(id: Pair<UuID, GrpID>)

}

class BasicNode : Node {

    override fun <T> create(withValue: T): Pair<UuID, GrpID> {
        TODO("Not yet implemented")
    }

    override fun <T> get(id: Pair<UuID, GrpID>): NodeInfo<T> {
        TODO("Not yet implemented")
    }

    override fun <T> update(n: NodeInfo<T>) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Pair<UuID, GrpID>) {
        TODO("Not yet implemented")
    }

}
