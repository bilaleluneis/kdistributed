/**
 * @author Bilal El Uneis
 * @since Sep 2022
 * bilaleluneis@gmail.com
 */

package functional

import common.Data
import common.DataOnly
import common.toDataOnly


sealed class FunctionalOps {
    abstract fun eval(input: List<Data>): List<Data>
}

class Filter<T>(val f: (T) -> Boolean) : FunctionalOps() {
    override fun eval(input: List<Data>): List<Data> {
        return input.toDataOnly<T>().filter { f(it.data) }
    }
}

class Map<T, R>(val m: (T) -> R) : FunctionalOps() {
    override fun eval(input: List<Data>): List<Data> {
        return input.toDataOnly<T>().map { DataOnly(m(it.data)) }
    }
}

class Reduce<T>(val r: (List<T>) -> T) : FunctionalOps() {
    override fun eval(input: List<Data>): List<Data> {
        val rInput = input.toDataOnly<T>().map { it.data }
        val result = r(rInput)
        return listOf(DataOnly(result))
    }
}
