

fun List<String>.getLanternfish(): Shoal = fold(mutableListOf()) {acc, s ->
    acc.addAll(s.split(",").toInts())
    acc
}

typealias GroupedShoal = Map<Int, Long>
fun Shoal.toGroupedShoal(): GroupedShoal = fold(mutableMapOf()) { acc, fish ->
    acc[fish] = (acc.get(fish) ?: 0L) + 1
    acc
}
fun GroupedShoal.newDay(): GroupedShoal {
    val day = mutableMapOf<Int, Long>()
    map {(k, v) ->
        var newK = k - 1
        if(newK < 0) {
            newK = 6
            day[8] = v
        }
        day[newK] = (day.get(newK) ?: 0L) + v
    }
    return day
}

fun GroupedShoal.applyDays(numDays: Int): GroupedShoal {
    var newShoal = this
    for (d in 1..numDays) {
        newShoal = newShoal.newDay()
    }
    return newShoal
}


typealias Fish = Int
typealias Shoal = List<Fish>

fun main() {
    fun part1(input: List<String>) =
        input.getLanternfish().toGroupedShoal().applyDays(80).values.sum()

    fun part2(input: List<String>): Long =
        input.getLanternfish().toGroupedShoal().applyDays(256).values.sum()

    val testInput = readInput("Day06_test")
    val part1Result = part1(testInput)
    check(part1Result == 5934L) { "Expected: 5934 but found $part1Result" }

    val part2Result = part2(testInput)
    check((part2Result) == 26984457539) { "Expected 26984457539 but is: $part2Result" }

    val input = readInput("Day06")
    println("Part1: " + part1(input))
    println("Part2: " + part2(input))
}
