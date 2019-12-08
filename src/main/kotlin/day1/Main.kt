package day1

import common.Utils
import kotlin.math.floor

object Main {

    private const val INPUT = "/day1/input.txt"

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = Utils.getFileFromResource(INPUT).readLines()

        val sum = lines
                .map(String::toDouble)
                .map(Main::calculateFuel)
                .sum()

        print("Total fuel required is $sum")
    }

    private fun calculateInitialFuel(mass: Double): Double {
        return floor((mass / 3)) - 2
    }

    private fun calculateFuel(mass: Double): Double {
        val fuel = floor((mass / 3)) - 2

        if (fuel > 0) {
            return fuel + calculateFuel(fuel)
        }
        return 0.0
    }
}