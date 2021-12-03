

fun String.binToDec() = Integer.parseInt(this, 2)

fun Pair<String, String>.total() = first.binToDec() * second.binToDec()

typealias PowerConsumption = Pair</* Gamma rate */ String, /* Epsilon Rate */ String>
typealias LifeSupportRating = Pair</* Oxygen Generator */ String, /* C02 Scrubber */ String>

/**
 * Partition a list of binary strings based on char position
 */
fun List<String>.partitionBinary(charPos: Int): Pair<List<String>, List<String>> =
    groupBy { it[charPos] }.let {
        Pair(it['0'] ?: emptyList(), it['1'] ?: emptyList())
    }

/**
 * Reduce a list of binary strings by frequency, either by most frequent or least frequent
 */
fun List<String>.reduceBinaryByFrequency(mostFrequent: Boolean = true, charPos: Int = 0): String {
    val f = first()
    if (size == 1 || charPos > f.length) {
        return f
    }
    val (grp0, grp1) = partitionBinary(charPos)
    return if ((grp0.size > grp1.size) xor !mostFrequent) {
        grp0.reduceBinaryByFrequency(mostFrequent, charPos + 1)
    } else {
        grp1.reduceBinaryByFrequency(mostFrequent, charPos + 1)
    }
}

fun main() {

    fun part1(input: List<String>): Int =
        input.first().foldIndexed(PowerConsumption("", "")) {index, acc, _ ->
            input.partitionBinary(index).let{ (grp0, grp1) ->
                val firstAddition = if (grp0.size > grp1.size) "1" else "0"
                val secondAddition = if (grp0.size > grp1.size) "0" else "1"
                PowerConsumption(acc.first + firstAddition, acc.second + secondAddition)
            }
        }.total()


    fun part2(input: List<String>) =
        LifeSupportRating(
            input.reduceBinaryByFrequency(true),
            input.reduceBinaryByFrequency(false)
        ).total()


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    val part1Result = part1(testInput)
    check(part1Result == 198) { "Expected: 198 but found $part1Result" }

    val part2Result = part2(testInput)
    check(part2Result == 230) { "Expected 230 but is: $part2Result" }

    val input = readInput("Day03")
    println("Part1: " + part1(input))
    println("Part2: " + part2(input))
}
