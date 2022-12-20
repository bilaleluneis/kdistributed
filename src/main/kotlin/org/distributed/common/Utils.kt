/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package org.distributed.common

enum class IDType(val length: Int) {
    GRPID(12),
    UUID(6),
    TYPEID(18),
}

fun genId(type: IDType): String {
    val combChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1 until type.length).map { combChars.random() }.joinToString("")
}
