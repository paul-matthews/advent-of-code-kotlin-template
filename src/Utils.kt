import arrow.core.Either
import arrow.core.Ior
import java.io.File
import java.lang.Exception
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readFileContents(name: String) = File("src", "$name.txt").readLines()

open class FileError(val err: Exception)
typealias TestInput = List<String>

suspend fun readFile(name: String): Either<FileError, TestInput> =
    try {
        Either.Right(File("src", "$name.txt").readLines())
    } catch (e: Exception) {
        Either.Left(FileError(e))
    }

fun <T> checkExpectedValue(actualValue: T, expectedValue: T) =
    check(actualValue == expectedValue) { "Expected: $expectedValue, ACTUAL: $actualValue"}

fun <T> Either<T, Int>.printSuccessOrHandle(prefix: String, handle: (T) -> Unit) = when(this) {
            is Either.Left -> handle(value)
            is Either.Right -> println("$prefix: $value")
        }
/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun String.splitFilterBlanks(delim: String) = split(delim).filter { it != "" }
fun List<String>.toListInts() = map { it.toInt() }