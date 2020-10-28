package sorting
import java.io.File
import java.lang.Exception
import java.util.*

val scanner = Scanner(System.`in`)
var input = mutableListOf<String>()
var type = "long"
var sort = "natural"
var isWork = true
var inputFile = ""
var outputFile = ""
var isLine = false

fun main(args: Array<String>) {
    processArgs(args)
    if (!isWork) return
    if (inputFile == "") {
        while (scanner.hasNext()) {
            input.add(scanner.nextLine())
        }
    } else {
        import(inputFile)
    }
    when (type) {
        "long" -> processAsLongs(input)
        "line" -> processAsLines(input)
        "word" -> processAsWords(input)
    }
}

fun processAsLongs(input: List<String>) {
    var longs = mutableListOf<Long>()
    input.forEach { x -> x.split("\\s+".toRegex()).filter {
        val isLong =  it.matches("[+-]*\\d+".toRegex())
        if (!isLong) println("\"$it\" is not a long. It will be skipped.")
        isLong }.
    map { it.toLong() }.forEach { longs.add(it) } }
    when (sort) {
        "natural" -> printNat(sortNatural(longs), longs.size)
        "byCount" -> printCount(sortByCount(longs), longs.size)
    }
}

fun processAsLines(input: List<String>) {
    isLine = true
    val sortNatural = sortNatural(input)
    when (sort) {
        "natural" -> printNat(sortNatural(input), input.size)
        "byCount" -> printCount(sortByCount(input), input.size)
    }
}

fun percent(x: Int, y: Int): Int = x * 100 / y

fun processAsWords(input: List<String>) {
    var words = mutableListOf<String>()
    input.forEach { x -> x.split("\\s+".toRegex()).map { it }.forEach { words.add(it) } }
    when (sort) {
        "natural" -> printNat(sortNatural(words), words.size)
        "byCount" -> printCount(sortByCount(words), words.size)
    }
}

fun <T : Comparable<T>> printNat(input: List<T>, size: Int) {
    var res = ""
    res += "Total numbers: $size.\n"
    if (!isLine) {
        res += "Sorted data: "
        input.forEach { x -> res += "$x " }
    }
    else {
        res += "Sorted data:\n"
        input.forEach { x -> res += "$x\n" }
    }
    if (outputFile == "") {
        println(res)
    } else {
        export(outputFile, res)
    }
}

fun <T : Comparable<T>> printCount(input: Map<T, Int>, size: Int) {
    var res = ""
    res += "Total numbers: $size.\n"
    input.forEach { (k, v) -> res += "$k: $v time(s), ${percent(v, size)}%\n" }
    if (outputFile == "") {
        println(res)
    } else {
        export(outputFile, res)
    }
}

fun processArgs(args: Array<String>) {
    if (args.isNotEmpty()) {
        for (i in args.indices) {
            when {
                args[i] == "-sortingType" -> {
                    when {
                        args.size == i + 1 -> argsError("sorting type")
                        args[i + 1] != "natural" && args[i + 1] != "byCount" -> argsError("sorting type")
                        else -> sort = args[i + 1]
                    }
                }
                args[i] == "-dataType" -> {
                    when {
                        args.size == i + 1 -> argsError("data type")
                        args[i + 1] != "long" && args[i + 1] != "line" && args[i + 1] != "word" -> argsError("data type")
                        else -> type = args[i + 1]
                    }
                }
                args[i] == "-inputFile" -> {
                    when {
                        args.size == i + 1 -> argsError("data type")
                        else -> inputFile = args[i + 1]
                    }
                }
                args[i] == "-outputFile" -> {
                    when {
                        args.size == i + 1 -> argsError("data type")
                        else -> outputFile = args[i + 1]
                    }
                }
                else -> {
                    if (args[i] != "long" && args[i] != "line" && args[i] != "word" &&
                            args[i] != "natural" && args[i] != "byCount") {
                        println("\"${args[i]}\" is not a valid parameter. It will be skipped. ")
                    }
                }
            }
        }
    }
}

fun argsError(type: String) {
    println("No $type defined!")
    isWork = false
}

fun <T : Comparable<T>> sortNatural(input: List<T>): List<T> {
    return input.sorted()
}

fun <T : Comparable<T>> sortByCount(input: List<T>): Map<T, Int> {
    return input.sorted().groupingBy { it }.eachCount().toList().sortedBy { (_, value) -> value}.toMap()
}

fun import(fileName: String) {
    try {
        val lines = File(fileName).readLines()
        for (line in lines) {
            input.add(line)
        }
    } catch (e: Exception) {
        println("File not found.")
    }
}

fun export(fileName: String, result: String) {
    val myFile = File(fileName).writeText(result)
}