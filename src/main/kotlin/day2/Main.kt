package day2

import common.Utils
import common.Utils.getInputList

object Main {

    private const val INPUT = "/day2/input.txt"

    private const val EXIT = 99

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = Utils.getFileFromResource(INPUT).readLines()

        val numbers = getInputList(lines)
                .map(String::toInt)
                .toMutableList()

        val result = executeProgram(numbers)

        println("Result for 2 and 12 is ${result[0]}")

        getExpectedResult(numbers, 19690720)
    }

    private fun getExpectedResult(input : MutableList<Int>, expected : Int) {
        for (noun in 0..99) {
            for (verb in 0..99) {
                input[1] = noun
                input[2] = verb

                val result = executeProgram(input)

                if (result[0] == expected) {
                    println("To obtain $expected the noun was $noun and the verb was $verb")
                    break
                }
            }
        }
    }

    private fun executeProgram(input : List<Int>) : MutableList<Int> {
        val output = input.toMutableList()
        val x = 1

        for (i in output.indices step x) {
            if (output[i] == EXIT) break

            val instruction = output.subList(i, i+4)
            val code = instruction[0]
            val a = output[instruction[1]]
            val b = output[instruction[2]]
            val c = instruction[3]

            val result = when (code) {
                1 -> a + b
                2 -> a * b
                else -> {
                    throw UnsupportedOperationException("Opcode $code is unsupported")
                }
            }

            output[c] = result
        }

        return output
    }
}