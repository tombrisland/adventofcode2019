package day7

import common.intcode.Program
import common.Utils
import common.Utils.getInputList
import java.util.*

object Main {

    private const val INPUT = "/day7/input.txt"

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = Utils.getFileFromResource(INPUT).readLines()

        val instructions = getInputList(lines)
                .map(String::toLong)

        partOne(instructions)
//        partTwo(instructions)
    }

    private fun partTwo(instructions: List<Long>) {
        val combinations = generateCombinations(listOf(5, 6, 7, 8, 9))

        val outputs = combinations.map { phases ->

            val amplifiers = phases.map { Program(instructions, it) }

            var input = 0L

            while (amplifiers.all { !it.isExited }) {
                input = amplifiers.fold(input) { acc, amp -> amp.execute(acc) }
            }

            input
        }

        println("For part 2 The highest output was ${outputs.max()}")
    }

    private fun partOne(instructions: List<Long>) {
        val combinations = generateCombinations(listOf(0, 1, 2, 3, 4))

        val outputs = combinations.map { phases ->
            val amplifiers = phases.map { Program(instructions, it) }

            amplifiers.fold(0L) { acc, amp -> amp.execute(acc) }
        }

        println("For part 1 the highest output was ${outputs.max()}")
    }

    private fun <T> generateCombinations(items: List<T>): List<List<T>> {
        return generateCombinations(items.toMutableList(), 0, items.size - 1)
    }

    private fun <T> generateCombinations(items: MutableList<T>, l: Int, r: Int): List<List<T>> {
        val combinations = mutableListOf<List<T>>()

        if (l == r) {
            combinations.add(items.toList())
        } else {
            for (i in l..r) {
                Collections.swap(items, l, i)
                combinations.addAll(generateCombinations(items, l + 1, r))
                Collections.swap(items, l, i)
            }
        }

        return combinations
    }
}