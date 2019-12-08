package day5

import common.Instruction
import common.Program
import common.Utils

object Main {

    private const val INPUT = "/day5/input.txt"

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = Utils.getFileFromResource(INPUT).readLines()

        val numbers = lines
                .reduce { acc, s -> acc + s }
                .split(",")
                .map(String::toInt)
                .toMutableList()

        val program = Program(numbers)

        println("The for input 1 the result was was ${program.execute(1)}")
        println("The for input 5 the result was was ${program.execute(5)}")
    }


}