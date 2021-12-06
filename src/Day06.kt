

fun List<String>.getLanternfish(): Shoal = fold(mutableListOf()) {acc, s ->
    acc.addAll(s.split(",").toInts())
    acc
}

typealias Fish = Int
fun Fish.newDay(): Pair<Fish, Fish?>{
    val newVal = this - 1
    if (newVal < 0) {
        return Pair(6, 8)
    }
    return Pair(newVal, null)
}
typealias Shoal = List<Fish>
fun Shoal.newDay(): Shoal {
    val newFish = mutableListOf<Fish>()
    return map {fish ->
        val newDay = fish.newDay()
        newDay.second?.let {
            newFish.add(it)
        }
        newDay.first
    } + newFish
}
fun Shoal.applyDays(numDays: Int): Shoal {
    var newShoal = this
    for (d in 1..numDays) {
        newShoal = newShoal.newDay()
    }
    return newShoal
}

fun main() {
    fun part1(input: List<String>) =
        input.getLanternfish().applyDays(80).size

//    fun part2(input: List<String>): Long =
//        input.getLanternfish().applyDays(256).fold(0) {acc, _ ->
//            acc + 1
//        }
//
    val testInput = readInput("Day06_test")
    val part1Result = part1(testInput)
    check(part1Result == 5934) { "Expected: 5934 but found $part1Result" }

//    val part2Result = part2(testInput)
//    check((part2Result) == 26984457539) { "Expected 26984457539 but is: $part2Result" }

    val input = readInput("Day06")
    println("Part1: " + part1(input))
//    println("Part2: " + part2(input))
}
