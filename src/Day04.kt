

typealias BingoNumbers = List<Int>
fun List<String>.getBingoNumbers(): BingoNumbers = first().split(",").map { it.toInt() }

data class BingoBoard(val raw: RawBingoBoard, val scoreLines: List<BingoScoreLine>,
                      val matched: BingoNumbers, val unmatched: BingoNumbers) {
    companion object {
        fun fromRawBingoBoard(raw: RawBingoBoard): BingoBoard {
            return BingoBoard(
                raw = raw,
                scoreLines = raw.getScoreLines(),
                matched = emptyList(),
                unmatched = raw.flatten()
            )
        }
    }
    fun mark(num: Int): BingoBoard {
        var hasMatched = false
        val newScores = scoreLines.map {
            val newScore = it.mark(num)
            if (newScore !== it) hasMatched = true
            newScore
        }
        if (!hasMatched) {
            return this
        }
        return copy(scoreLines = newScores, matched = matched + num, unmatched = unmatched.filter { it != num })
    }

    fun hasWon() = scoreLines.any { it.hasWon() }
    fun score(): Int {
        return unmatched.sum() * matched.last()
    }
}

typealias RawBingoBoard = List<List<Int>>
fun List<String>.getRawBoards(): List<RawBingoBoard> {
    var remainingLines = drop(1)
    val boards = mutableListOf<RawBingoBoard>()
    while (remainingLines.isNotEmpty()) {
        val (newRemain, currentBoard) = remainingLines.getNextBoardAndRemainder()
        boards.add(currentBoard)
        remainingLines = newRemain
    }
    return boards
}
fun List<RawBingoBoard>.getBingoBoards() =
    map {
        BingoBoard.fromRawBingoBoard(it)
    }
fun RawBingoBoard.getScoreLines(): List<BingoScoreLine> {
    val scoreLines = mutableListOf<BingoScoreLine>()
    /* Add Rows */
    scoreLines.addAll(map { BingoScoreLine(0, it) })
    /* Add Columns */
    scoreLines.addAll(first().indices.map {index ->
        BingoScoreLine(0, map { it[index] })
    })
    return scoreLines
}

fun List<String>.getNextBoardAndRemainder(): Pair<List<String>, RawBingoBoard> {
    val rows = drop(1).takeWhile { it != "" }.map() {
        it.splitFilterBlanks(" ").toInts()
    }
    return Pair(drop(1).dropWhile { it != "" }, rows)
}
typealias BingoGame = Pair</* The game numbers */ BingoNumbers, /* The Score Boards */ List<BingoBoard>>
fun List<String>.getBingoGame() = BingoGame(
    getBingoNumbers(),
    getRawBoards().getBingoBoards())

typealias BingoScoreLine = Pair</* Score */ Int, /* Remaining Numbers */ List<Int>>
fun BingoScoreLine.hasWon(): Boolean = (second.size == 0)
fun BingoScoreLine.mark(num: Int): BingoScoreLine =
    if (hasWon() || !second.contains(num))
        this
    else
        BingoScoreLine(first + num, second.filter { it != num })

fun BingoGame.countWins(): Int = second.count { it.hasWon() }

fun BingoGame.scoreLastWin(): Int {
    if (first.isEmpty()) {
        return second.minOf { it.score() }
    }
    val drawnNumber = first.first()
    val previousWins = countWins()
    return BingoGame(first.drop(1), second.map {
        val marked = it.mark(drawnNumber)
        val previousWinState = it.hasWon()
        if (((previousWins + 1) == second.size) && previousWinState != marked.hasWon()) {
            return marked.score()
        }
        marked
    }).scoreLastWin()
}

fun BingoGame.score(): Int {
    if (first.isEmpty()) {
        return second.maxOf { it.score() }
    }
    val drawnNumber = first.first()
    return BingoGame(first.drop(1), second.map {
        val marked = it.mark(drawnNumber)
        if (marked.hasWon()) {
            return marked.score()
        }
        marked
    }).score()
}

fun main() {
    fun part1(input: List<String>) = input.getBingoGame().score()

    fun part2(input: List<String>) =
        input.getBingoGame().scoreLastWin()

    val testInput = readInput("Day04_test")
    val part1Result = part1(testInput)
    check(part1Result == 4512) { "Expected: 4512 but found $part1Result" }

    val part2Result = part2(testInput)
    check(part2Result == 1924) { "Expected 1924 but is: $part2Result" }

    val input = readInput("Day04")
    println("Part1: " + part1(input))
    println("Part2: " + part2(input))
}
