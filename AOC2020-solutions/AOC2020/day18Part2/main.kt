package day18Part2

import java.io.File
import java.lang.IllegalArgumentException

/*
Challenge at https://adventofcode.com/2020/day/18#part2

Now, addition and multiplication have different precedence levels, but they're not the ones you're familiar with.
Instead, addition is evaluated before multiplication.
*/

enum class Operation {
    ADD, MUL, NOP
}

data class Member(val number: Long, val operation: Operation)

class Expression(input: String) {
    val value: Long
        get() = evaluate()
    private val members = ArrayList<Member>()

    init {
        parse(input)
    }

    private fun parse(input: String) {
        var left = input
        var lastOperation = Operation.NOP
        while (left.isNotEmpty()) {
            left = left.trim()
            when {
                left.startsWith('(') -> {
                    var endI = findIOfMatchingClosingParenthesis(left)
                    members.add(Member(Expression(left.substring(1, endI)).value, lastOperation))
                    left = left.substring(endI + 1)
                }
                left.matches("""^\d+.*""".toRegex()) -> {
                    val numeric = """^(\d+)""".toRegex().find(left)!!.groupValues[1]
                    members.add(Member(numeric.toLong(), lastOperation))
                    left = left.substring(numeric.length)
                }
                left.startsWith('+') || left.startsWith('*') -> {
                    lastOperation = if (left[0] == '+') Operation.ADD else Operation.MUL
                    left = left.substring(1)
                }
            }
        }
    }

    private fun evaluate(): Long {
        var i = 0
        while (i in members.indices) {
            if (members[i].operation == Operation.ADD) {
                members[i - 1] = Member(members[i].number + members[i - 1].number, members[i - 1].operation)
                members.removeAt(i)
            } else
                i += 1
        }

        var result = 0L
        members.forEach { m ->
            when (m.operation) {
                Operation.ADD -> throw IllegalStateException("+ operations must not be present at this point!\n$members")
                Operation.MUL -> result *= m.number
                Operation.NOP -> result += m.number
            }
        }

        return result
    }
}

fun main() {
    val expressionList = getExpressionsFromFile("in.txt")
    var sum = 0L
    expressionList.forEach { expr ->
        println("${expr.value}")
        sum += expr.value
    }
    println("Sum: $sum")
}

fun getExpressionsFromFile(fileName: String): List<Expression> {
    val currentDir = System.getProperty("user.dir") + "/src/day18Part2"
    val buffReader = File("$currentDir/$fileName").bufferedReader()

    val expressions = ArrayList<Expression>()
    buffReader.lines().forEach { line ->
        expressions.add(Expression(line))
    }
    return expressions
}

fun findIOfMatchingClosingParenthesis(input: String): Int {
    if (!input.startsWith('('))
        throw IllegalArgumentException("String did not start with '(': $input")

    var balance = 1
    for (i in 1 until input.length) {
        when (input[i]) {
            '(' -> balance += 1
            ')' -> balance -= 1
        }
        if (balance == 0)
            return i
    }

    throw IllegalArgumentException("Could not find matching closing parenthesis in: $input")
}