package common

import common.Utils.range as range

class Line(val start: Position, val dX: Int, val dY: Int) {
    constructor(start: Position, end: Position) : this(start, end.x-start.x, end.y-start.y)

    private val xRange = range(start.x, start.x+dX)
    private val yRange = range(start.y, start.y+dY)

    fun isPositionOnLine(position: Position): Boolean {
        return xRange.contains(position.x) && yRange.contains(position.y)
    }

    val end = Position(start.x + dX, start.y + dY)

    override fun toString(): String {
        return "Line(start=$start, dX=$dX, dY=$dY, end=$end)"
    }
}