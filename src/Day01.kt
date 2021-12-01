
typealias IntPair = Pair<Int, Int>
fun List<String>.toInts(): List<Int> = map{ it.toInt() }
fun Iterable<IntPair>.countIncreasing() = count {(a, b) -> b > a}
fun main() {
    fun part1(input: List<String>): Int =
        input.toInts().zipWithNext().countIncreasing()

    fun part2(input: List<String>): Int =
        input.toInts().windowed(3).zipWithNext {a, b -> IntPair(a.sum(), b.sum()) }.countIncreasing()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 1)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
