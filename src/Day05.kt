import kotlin.math.sign

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
fun OverlapMap.get(coord: Coordinate) = get(coord.second)[coord.first]
fun OverlapMap.set(coord: Coordinate, value: Int) {
    get(coord.second)[coord.first] = value
}

fun OverlapMap.println() = map {
    println(it.joinToString())
    this
}

typealias HydrothermalMap = Pair</* Lines */ List<Line>, /* Matches */ OverlapMap>
fun List<Line>.createMap(): HydrothermalMap {
    val coords = map { it.first } + map { it.second }
    val maxX = coords.maxOf { it.first }
    val maxY = coords.maxOf { it.second }
    return HydrothermalMap(
        this,
        Array(maxY + 1) { Array(maxX + 1) { 0 } }
    )
}
fun OverlapMap.markLine(line: Line): OverlapMap {
    val markings = clone()
    val start = line.first
    val end = line.second
    val incX = sign((end.first - start.first).toDouble()).toInt()
    val incY = sign((end.second - start.second).toDouble()).toInt()
    var curX = start.first
    var curY = start.second
    while (true) {
        markings.apply {
            get(curY)[curX] = get(curY)[curX] + 1
        }
        if (curX == end.first && curY == end.second)
            break
        curX += incX
        curY += incY
    }
    return markings
}
fun HydrothermalMap.getOverlapPoints(): List<Int> {
    var markings = second
    first.map { markings = second.markLine(it) }
    return markings.flatten()
}

fun main() {
    fun part1(input: List<String>) =
        input
            .toNearbyVents()
            .createMap()
            .getOverlapPoints()
            .count { it > 1 }

    fun part2(input: List<String>): Int =
        input
            .toNearbyVents(true)
            .createMap()
            .getOverlapPoints()
            .count { it > 1 }

    val testInput = readFileContents("Day05_test")
    val part1Result = part1(testInput)
    check(part1Result == 5) { "Expected: 5 but found $part1Result" }

    val part2Result = part2(testInput)
    check((part2Result) == 12) { "Expected 12 but is: $part2Result" }

    val input = readFileContents("Day05")
    println("Part1: " + part1(input))
    println("Part2: " + part2(input))
}
