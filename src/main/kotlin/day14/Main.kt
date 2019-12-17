package day14

import common.Utils
import day14.Reaction.Chemical
import kotlin.math.ceil

object Main {

    private const val INPUT = "/day14/input.txt"

    @JvmStatic
    fun main(args: Array<String>) {

        val reactions = Utils.getFileFromResource(INPUT)
                .readLines()
                .map(Reaction.Companion::parseString)


        val oreRequired = calculateOreRequired(reactions)

        println("The output required $oreRequired ore to produce 1 FUEL")

        val fuelProduced = maxFuelProduced(reactions, 1000000000000.0)

        println("The fuel produced with 1 trillion ORE is $fuelProduced")
    }

    private fun calculateOreRequired(reactions : List<Reaction>) : Double {
        val nanoFactory = Nanofactory(reactions)

        return nanoFactory.createChemical(Chemical("FUEL"), 1.0)
    }

    private fun maxFuelProduced(reactions: List<Reaction>, oreCount: Double) : Double {
        var step = 50000.0
        var start = 0.0

        while (true) {
            val result = maxFuelProduced(reactions, oreCount, start, step)

            if (step == 1.0) {
                return result
            } else {
                step = ceil(step / 2)
                start = result
            }
        }
    }

    private fun maxFuelProduced(reactions: List<Reaction>, oreCount: Double, start : Double, step : Double): Double {
        var output = 0.0
        var fuelCount = start
        var lastResult = fuelCount

        while (output != oreCount) {
            val nanoFactory = Nanofactory(reactions)

            output = nanoFactory.createChemical(Chemical("FUEL"), fuelCount)

            if (output > oreCount) {
                return lastResult
            }

            lastResult = fuelCount
            fuelCount+=step
        }

        throw RuntimeException()
    }

}