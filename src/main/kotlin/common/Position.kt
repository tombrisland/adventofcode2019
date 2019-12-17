package common

import day11.Main
import day11.Main.Orientation.NORTH

class Position(val x: Int, val y: Int) {

    var orientation = NORTH

    fun above() = surroundings()[orientation.ordinal]
    fun below() = surroundings()[orientation.turn180().ordinal]
    fun left() = surroundings()[orientation.turnLeft().ordinal]
    fun right() = surroundings()[orientation.turnRight().ordinal]

    fun surroundings(): List<Position> {
        return listOf(
                Position(x, y + 1),
                Position(x + 1, y),
                Position(x, y - 1),
                Position(x - 1, y)
        )
    }

    override fun toString(): String {
        return "Position(x=$x, y=$y)"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Position) return false

        return (x == other.x && y == other.y)
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y + orientation.ordinal
        return result
    }
}