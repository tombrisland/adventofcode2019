package day9

import common.intcode.Program
import common.Utils
import common.Utils.getInputList

object Main {

    private const val INPUT = "/day9/input.txt"

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = Utils.getFileFromResource(INPUT).readLines()

        val instructions = getInputList(lines)
                .map(String::toLong)

        println("The BOOST keycode is ${Program(instructions).execute(1)}")
        println("The coordinates of the distress signal are ${Program(instructions).execute(2)}")
    }
}