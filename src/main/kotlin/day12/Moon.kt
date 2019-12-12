package day12

import java.util.regex.Pattern
import kotlin.math.abs

class Moon(val point: Point, val velocity: Velocity) {

    fun applyVelocity() : Moon {
        val p = Point(
                point.x + velocity.x,
                point.y + velocity.y,
                point.z + velocity.z
        )

        return Moon(p, velocity)
    }

    fun calculateGravity(other : Point) : Velocity {
        return Velocity(
                calculateDV(velocity.x, point.x, other.x),
                calculateDV(velocity.y, point.y, other.y),
                calculateDV(velocity.z, point.z, other.z))
    }

    private fun calculateDV(v : Int, a : Int, b : Int) : Int {
        return when {
            a < b -> v + 1
            a > b -> v - 1
            else -> v
        }
    }

    fun calculateEnergy() : Int {
        return calculatePotentialEnergy() * calculateKineticEnergy()
    }

    private fun calculatePotentialEnergy() : Int {
        return abs(point.x) + abs(point.y) + abs(point.z)
    }

    private fun calculateKineticEnergy() : Int {
        return abs(velocity.x) + abs(velocity.y) + abs(velocity.z)
    }

    companion object {
        private val PLANET_REGEX = Pattern.compile("^<x=(-?[0-9]+), y=(-?[0-9]+), z=(-?[0-9]+)>$")

        fun calculateGravity(moon : Moon, other : Moon) : Moon {
            return Moon(moon.point, moon.calculateGravity(other.point))
        }

        fun fromString(string : String) : Moon {
            val matcher = PLANET_REGEX.matcher(string)
            matcher.find()

            val point = Point(
                    matcher.group(1).toInt(),
                    matcher.group(2).toInt(),
                    matcher.group(3).toInt())

            return Moon(point, Velocity(0, 0, 0))
        }
    }

    override fun toString(): String {
        return "Moon(point=$point, velocity=$velocity)"
    }
}