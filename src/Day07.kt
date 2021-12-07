import java.lang.Math.abs

typealias CrabMultiplier = (Int) -> Int
val constantInt: CrabMultiplier = {i -> i}
val incrementalInt: CrabMultiplier = {i -> (1..i).toList().sum()}

fun List<String>.toCrabs(): Crabs = fold(mutableListOf()) { acc, s ->
    acc.addAll(s.split(",").toInts())
    acc
}
typealias Crabs = List<Int>
fun Crabs.getDistanceFrom(point: Int, multiplier: CrabMultiplier) = fold(0) {acc, crab->
    acc + multiplier(abs(point - crab))
}
fun Crabs.getDistanceFrom(points: List<Int>, multiplier: CrabMultiplier) = points.map { getDistanceFrom(it, multiplier) }
fun Crabs.getMinMaxRange() = minOf { it }..maxOf { it }

fun main() {
    fun part1(input: List<String>) = with(input.toCrabs()) {
        getDistanceFrom((getMinMaxRange()).toList(), constantInt).minOf { it }
    }


    fun part2(input: List<String>) = with(input.toCrabs()) {
        getDistanceFrom((getMinMaxRange()).toList(), incrementalInt).minOf { it }
    }

    val testInput = readInput("Day07_test")
    val part1Result = part1(testInput)
    println(part1Result)
    check(part1Result == 37) { "Expected: 37 but found $part1Result" }

    val part2Result = part2(testInput)
    check((part2Result) == 168) { "Expected 168 but is: $part2Result" }

    val input = readInput("Day07")
    println("Part1: " + part1(input))
    println("Part2: " + part2(input))
}
