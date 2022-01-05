import arrow.core.Either
import arrow.core.computations.either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis


typealias IntPair = Pair<Int, Int>
interface Day01 {
    suspend fun part1(input: String): Either<Day01Errors, Int> =
        either {
            val contents = input.readFileContents().bind()
            val listInts = contents.toInts().bind()
            listInts
                .zipWithNext()
                .countIncreasing()
        }

    suspend fun part2(input: String): Either<Day01Errors, Int> =
        either {
            val contents = input.readFileContents().bind()
            val listInts = contents.toInts().bind()
            listInts
                .windowed(3)
                .zipWithNext {a, b -> IntPair(a.sum(), b.sum()) }
                .countIncreasing()
        }

    suspend fun String.readFileContents(): Either<Day01Errors, List<String>> =
        readFile(this)
            .mapLeft { Day01Errors.FileErr() }

    fun List<String>.toInts(): Either<Day01Errors, List<Int>> =
            Either
                .catch { map { it.toInt() } }
                .mapLeft { Day01Errors.NumberErr() }

    fun Iterable<IntPair>.countIncreasing() = count {(a, b) -> b > a}

    sealed class Day01Errors {
        open val printString = ""
        override fun toString(): String {
            return "ERROR: $printString"
        }
        class FileErr: Day01Errors() {
            override val printString = "Error loading file"
        }
        class NumberErr: Day01Errors() {
            override val printString = "Error reading file contents"
        }
    }
}

fun main() {
    val day01 = object : Day01 {}

    runBlocking {
        val time = measureTimeMillis {
            // TESTS:
            val part1Test = async(Dispatchers.IO) { day01.part1("Day01_test") }
            val part2Test = async(Dispatchers.IO) { day01.part2("Day01_test") }

            // REAL DATA:
            val part1Real = async(Dispatchers.IO) { day01.part1("Day01") }
            val part2Real = async(Dispatchers.IO) { day01.part2("Day01") }

            part1Test.await()
                .map { checkExpectedValue(it, 7) }
                .mapLeft { check(false) { "Failed with error: $it" } }

            part2Test.await()
                .map { checkExpectedValue(it, 5) }
                .mapLeft { check(false) { "Failed with error: $it" } }

            part1Real.await()
                .map { println("Day01.part1: $it") }
                .mapLeft { println(it) }

            part2Real.await()
                .map { println("Day01.part2: $it") }
                .mapLeft { println(it) }

        }
        println("Complete in $time ms")
    }
}
