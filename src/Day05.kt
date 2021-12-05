import java.util.*

val VENT_LINE_FORMAT = """^(\d+),(\d+)\s*->\s*(\d+),(\d+)$""".toRegex()
typealias Coordinate = Pair<Int, Int>
typealias Line = Pair<Coordinate, Coordinate>
fun Line.isNotDiagonal(): Boolean = ((first.first == second.first) or (first.second == second.second))

val MatchResult.groupIntValues: List<Int>
    get() = groupValues.drop(1).map { it.toInt() }

fun List<String>.toNearbyVents(allowDiagonal: Boolean = false): List<Line> =
    fold(mutableListOf()) {acc, line ->
        VENT_LINE_FORMAT.find(line)?.let {mr ->
            if (mr.groups.size == 5) {
                val currentLine = Line(Coordinate(mr.groupIntValues[0], mr.groupIntValues[1]),
                    Coordinate(mr.groupIntValues[2], mr.groupIntValues[3]))
                if (allowDiagonal || currentLine.isNotDiagonal()) {
                    acc.add(currentLine)
                }
            }
        }
        acc
    }
typealias OverlapMap = Array<Array<Int>>
typealias HydrothermalMap = Pair</* Lines */ List<Line>, /* Matches */ OverlapMap>
fun List<Line>.createMap(): HydrothermalMap {
    val coords = map { it.first } + map { it.second }
    val maxX = coords.maxOf { it.first }
    val maxY = coords.maxOf { it.second }
    return HydrothermalMap(
        this,
        Array(maxX + 1) { Array(maxY + 1) { 0 } }
    )
}
fun OverlapMap.markLine(line: Line): OverlapMap {
    val start = line.first
    val end = line.second
    val (startX, endX) = if (start.first < end.first) Pair(start.first, end.first) else Pair(end.first, start.first)
    val (startY, endY) = if (start.second < end.second) Pair(start.second, end.second) else Pair(end.second, start.second)

    val markings = clone()
    (startX..endX).map {x ->
        (startY..endY).map {y ->
            markings.apply {
                get(x)[y] = get(x)[y] + 1
            }
        }
    }
    return markings
}
fun HydrothermalMap.getOverlapPoints(): List<Int> {
    var markings = second
    first.map { markings = second.markLine(it) }
    markings.map {
    }
    return markings.flatten()
}

fun main() {
    fun part1(input: List<String>) =
        input
            .toNearbyVents()
            .createMap()
            .getOverlapPoints()
            .count { it > 1 }

//    fun part2(input: List<String>): Int =
//        input.size

    val testInput = readInput("Day05_test")
    val part1Result = part1(testInput)
    check(part1Result == 5) { "Expected: 5 but found $part1Result" }

//    val part2Result = part2(testInput)
//    check(part2Result == 900, { "Expected 900 but is: $part2Result"})

    val input = readInput("Day05")
    println("Part1: " + part1(input))
//    println("Part2: " + part2(input))
}
