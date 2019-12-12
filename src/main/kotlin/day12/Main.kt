package day12

import common.Utils

object Main {

    private const val INPUT = "/day12/input.txt"

    @JvmStatic
    fun main(args: Array<String>) {

        val moons = Utils.getFileFromResource(INPUT)
                .readLines()
                .map { Moon.fromString(it) }


        val partOne = runSimulation(moons, 1000)
                .sumBy(Moon::calculateEnergy)

        println("The total energy after 1000 iterations is $partOne")

        val cycles = calculateCycles(moons)

        println("The system repeats after ${calculateFactor(cycles)} cycles")

    }

    private fun calculateFactor(cycles : Map<Char, Long>) : Long {
        val values = cycles.values.toList()
        return lcm(values[0], lcm(values[1], values[2]))
    }

    private fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
    private fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b

    private fun runSimulation(startingState: List<Moon>, iterations: Int): List<Moon> {
        var moons = startingState.toList()

        for (time in 0 until iterations) {

            moons = moons
                    .map { moon ->
                        moons
                                .filter { moon != it }
                                .fold(moon) { a, b -> Moon.calculateGravity(a, b) }
                    }
                    .map(Moon::applyVelocity)

        }

        return moons
    }

    private fun calculateCycles(startingState: List<Moon>): MutableMap<Char, Long> {
        var moons = startingState.toList()

        val cycles = mutableMapOf<Char, Long>()
        var steps = 0L

        while (cycles.size < 3) {

            moons = moons
                    .map { moon ->
                        moons
                                .filter { moon != it }
                                .fold(moon) { a, b -> Moon.calculateGravity(a, b) }
                    }
                    .map(Moon::applyVelocity)

            steps++

            if (steps > 1) {
                if (!cycles.containsKey('x') && isCycle(startingState, moons) { it.x }) cycles['x'] = steps
                if (!cycles.containsKey('y') && isCycle(startingState, moons) { it.y }) cycles['y'] = steps
                if (!cycles.containsKey('z') && isCycle(startingState, moons) { it.z }) cycles['z'] = steps
            }
        }

        return cycles
    }

    private fun isCycle(a: List<Moon>, b: List<Moon>, getField: (Dimensions3) -> Int): Boolean {
        return a.mapIndexed { i, moon ->
            getField(moon.point) == getField(b[i].point) &&
            getField(moon.velocity) == getField(b[i].velocity)
        }.all { it }
    }
}