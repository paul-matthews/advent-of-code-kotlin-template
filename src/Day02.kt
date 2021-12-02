
typealias Command = Pair<String, Int>
typealias DiveState = Triple< /* Horizontal */Int, /* Depth */ Int, /* Aim */ Int>

fun List<String>.toCommands() = map {
    with(it.split(" ")) { Command(first(), this[1].toInt()) }
}

fun DiveState.add(firstAdd: Int = 0, secondAdd: Int = 0, thirdAdd: Int = 0) =
    copy(first + firstAdd, second + secondAdd, third + thirdAdd)
fun DiveState.total() = first * second

fun main() {
    fun part1(input: List<String>): Int =
        input.toCommands().fold(DiveState(0, 0, 0)) {state, (cmd, distance) ->
            when(cmd) {
                "forward" -> state.add(firstAdd = distance)
                "backward" -> state.add(firstAdd = -distance)
                "up" -> state.add(secondAdd = -distance)
                "down" -> state.add(secondAdd = distance)
                else -> state
            }
        }.total()

    fun part2(input: List<String>): Int =
        input.toCommands().fold(DiveState(0, 0, 0)) {state, (cmd, distance) ->
            when(cmd) {
                "forward" -> state.add(firstAdd = distance, secondAdd = (distance * state.third))
                "backward" -> state.add(firstAdd = -distance)
                "up" -> state.add(thirdAdd = -distance)
                "down" -> state.add(thirdAdd = distance)
                else -> state
            }
        }.total()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    val part1Result = part1(testInput)
    check(part1Result == 150, {"Expected: 150 but found $part1Result"})

    val part2Result = part2(testInput)
    check(part2Result == 900, { "Expected 900 but is: $part2Result"})

    val input = readInput("Day02")
    println("Part1: " + part1(input))
    println("Part2: " + part2(input))
}
