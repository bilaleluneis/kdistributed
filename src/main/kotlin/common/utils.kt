/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package common

enum class IDType(val length: Int) {
    GRPID(12),
    UUID(6),
}

// TODO: introduce const or enum to hold length for uuid , grpid,etc
fun genId(type: IDType): String {
    val combChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1 until type.length).map { combChars.random() }.joinToString("")
}
