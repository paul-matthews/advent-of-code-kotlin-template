

typealias BingoNumbers = List<Int>
fun List<String>.getBingoNumbers(): BingoNumbers = first().split(",").map { it.toInt() }

typealias RawBingoBoard = List<List<Int>>

typealias BingoBoard = Pair</* 2D Board */ List<List<Int>>, /* Board Score */ Int>
fun List<String>.getNextBoardAndRemainder(): Pair<List<String>, BingoBoard> {
    val rows = drop(1).takeWhile { it != "" }.map() {
        it.splitFilterBlanks(" ").toInts()
    }
    return Pair(drop(1).dropWhile { it != "" }, Pair(rows, rows.sumOf { it.sum() }))
}
fun List<String>.getBoards(): List<BingoBoard> {
    var remainingLines = drop(1)
    val boards = mutableListOf<BingoBoard>()
    while (remainingLines.size > 0) {
        val (newRemain, currentBoard) = remainingLines.getNextBoardAndRemainder()
        boards.add(currentBoard)
        remainingLines = newRemain
    }
    return boards
}

typealias BingoGame = Pair</* The game numbers */ BingoNumbers, /* The Score Boards */ List<BingoScoreGroup>>
fun List<String>.getBingoGame() = BingoGame(
    getBingoNumbers(),
    getBoards()
        .getScoreGroups())

typealias BingoScoreGroup = Triple</* Score */ Int, /* Remaining Numbers */ List<Int>, /* Board Sum */ Int>
fun BingoScoreGroup.hasWon(): Boolean = (second.size == 0)
fun BingoScoreGroup.mark(num: Int): BingoScoreGroup =
    if (hasWon() || !second.contains(num))
        this
    else
        BingoScoreGroup(first + num, second.filter { it != num }, third)
fun BingoScoreGroup.getScore(): Int {
    println(this)
    return (third - first) * first
}

fun List<BingoBoard>.getScoreGroups() =
    fold(mutableListOf<BingoScoreGroup>()) {acc, (board, boardScore) ->
        /* Add Rows */
        acc.addAll(board.map {row ->
            BingoScoreGroup(0, row, boardScore)
        })
        /* Add Columns */
        acc.addAll(board.first().indices.map {index ->
            BingoScoreGroup(0, board.map { it[index] }, boardScore)
        })
        acc
    }

fun BingoGame.score(): Int {
    if (first.size < 1) {
        return second.maxByOrNull { it.first }?.getScore() ?: 0
    }
    val drawnNumber = first.first()
    return BingoGame(first.drop(1), second.map {
        val marked = it.mark(drawnNumber)
        if (marked.hasWon()) {
            return marked.getScore()
        }
        marked
    }).score()
}

fun main() {
    fun part1(input: List<String>) = input.getBingoGame().score()

//    fun part2(input: List<String>) =
//        input.size

    val testInput = readInput("Day04_test")
    val part1Result = part1(testInput)
    print(part1Result)
    check(part1Result == 4512) { "Expected: 4512 but found $part1Result" }

//    val part2Result = part2(testInput)
//    check(part2Result == 230) { "Expected 230 but is: $part2Result" }

//    val input = readInput("Day04")
//    println("Part1: " + part1(input))
//    println("Part2: " + part2(input))
}
