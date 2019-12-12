package day10

import common.Position
import common.Utils
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

object Main {

    private const val INPUT = "/day10/input.txt"

    private const val ASTEROID = '#'

    @JvmStatic
    fun main(args: Array<String>) {
        val grid = Utils.getFileFromResource(INPUT).readLines()

        val asteroids = calculateCoordinates(grid)

        val base = asteroids
                .map { Pair(it, calculateVisibleAsteroids(asteroids, it)) }
                .maxBy { it.second }!!

        println("The asteroid with the most visible asteroids was ${base.first} with ${base.second}")

        val targets = relativiseAsteroids(asteroids, base.first)
                .sortedWith(compareBy({ it.angle }, { it.distance }))
                .toMutableList()

        val angles = targets.map { it.angle }.toSet()

        val destroyed = mutableListOf<Asteroid>()

        while (targets.isNotEmpty()) {
            angles.forEach { angle ->
                val target = targets.find { it.angle == angle }

                target?.let {
                    targets.remove(it)
                    destroyed.add(it)
                }
            }
        }

        println("The 200th asteroid to be destroyed was ${destroyed[199].position}")
    }

    private fun calculateVisibleAsteroids(asteroids: List<Position>, position: Position): Int {
        return relativiseAsteroids(asteroids, position)
                .map { it.angle }.toSet().size
    }

    private fun relativiseAsteroids(asteroids: List<Position>, position: Position): List<Asteroid> {
        return asteroids
                .filter { it != position }
                .map { Asteroid(it, calculateDistance(position, it), calculateAngle(position, it)) }
    }

    private fun calculateDistance(a: Position, b: Position) : Double {
        val dY = abs(b.y - a.y).toDouble()
        val dX = abs(b.x - a.x).toDouble()

        return sqrt(dY.pow(2) + dX.pow(2))
    }

    private fun calculateAngle(a : Position, b : Position) : Double {
        val dY = a.y - b.y
        val dX = b.x - a.x

        // Angles will be from the horizontal
        val angle = (Math.toDegrees(atan2(dY.toDouble(), dX.toDouble())) + 270)

        return if (angle == 360.toDouble()) {
            0.toDouble()
        } else {
            return 360 - (angle % 360)
        }
    }

    private fun calculateCoordinates(grid: List<String>): List<Position> {
        return grid
                .mapIndexed { y, r ->
                    r.mapIndexed { x, c ->
                        if (c == ASTEROID) { Position(x, y) } else { null }
                    }.filterNotNull()
                }.flatten()
    }

    class Asteroid(val position : Position, val distance : Double, val angle : Double) {
        override fun toString(): String {
            return "Asteroid(position=$position, distance=$distance, angle=$angle)"
        }
    }
}