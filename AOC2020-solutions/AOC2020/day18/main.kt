package day18

import java.io.File
import java.lang.IllegalArgumentException

/*
Challenge at https://adventofcode.com/2020/day/18

The homework (your puzzle input) consists of a series of expressions that consist of addition (+), multiplication (*),
and parentheses ((...)). Just like normal math, parentheses indicate that the expression inside must be evaluated before
it can be used by the surrounding expression. Addition still finds the sum of the numbers on both sides of the operator,
and multiplication still finds the product.

However, the rules of operator precedence have changed. Rather than evaluating multiplication before addition, the operators
have the same precedence, and are evaluated left-to-right regardless of the order in which they appear.
 */

data class Member(val number: Long, val operation: Char)

class Expression(input: String) {
    val value: Long
        get() = evaluate()
    private val members = ArrayList<Member>()

    init {
        parse(input)
    }

    private fun parse(input: String) {
        var left = input
        var lastOperation = '+'
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
                    lastOperation = left[0]
                    left = left.substring(1)
                }
            }
        }
    }

    private fun evaluate(): Long {
        var result = 0L
        members.forEach { m ->
            when (m.operation) {
                '+' -> result += m.number
                '*' -> result *= m.number
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
    val currentDir = System.getProperty("user.dir") + "/src/day18"
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