package day3

import common.Line
import common.Position
import common.Utils
import java.util.regex.Pattern
import kotlin.math.abs

object Main {

    private const val INPUT = "/day3/input.txt"

    private val INSTRUCTION_REGEX = Pattern.compile("([A-Z])([0-9]+)")
    private const val OPERATION_GROUP = 1
    private const val OPERAND_GROUP = 2

    @JvmStatic
    fun main(args: Array<String>) {
        val wires = Utils.getFileFromResource(INPUT).readLines()
                .map { wire -> wire.split(",") }
                .map(this::wireToLines)

        val wireOne = wires[0]
        val wireTwo = wires[1]

        val intersections = mutableListOf<Pair<Position, Int>>()

        wireOne.forEachIndexed { i, a ->
            wireTwo.forEachIndexed { j, b ->
                val currentSteps = calculateStepCount(wireOne, wireTwo, i, j)
                val intersection = findIntersection(a, b)

                intersection?.let {
                    intersections.add(Pair(it, currentSteps +
                            calculateStepCount(Line(a.start, intersection)) +
                            calculateStepCount(Line(b.start, intersection))))
                }
            }
        }

        intersections.sortBy { manhattanDistanceFromZero(it.first) }
        println("The intersection closest to 0,0 was ${intersections[0].first}")
        intersections.sortBy { it.second }
        println("The intersection with the least steps was ${intersections[0].first}")
    }

    private fun manhattanDistanceFromZero(a: Position): Int {
        return abs(a.y) + abs(a.x)
    }

    private fun calculateStepCount(a: List<Line>, b: List<Line>, i: Int, j: Int): Int {
        var stepCount = 0
        a.subList(0, i).forEach { stepCount += calculateStepCount(it) }
        b.subList(0, j).forEach { stepCount += calculateStepCount(it) }

        return stepCount;
    }

    private fun calculateStepCount(line: Line): Int {
        return abs(line.dX) + abs(line.dY)
    }

    private fun findIntersection(a: Line, b: Line): Position? {
        if (a.dX == 0 && b.dY == 0) {
            val intersect = calculateIntersection(a, b)

            if (a.isPositionOnLine(intersect) && b.isPositionOnLine(intersect)) return intersect
        } else if (b.dX == 0 && a.dY == 0) {
            val intersect = calculateIntersection(b, a)

            if (a.isPositionOnLine(intersect) && b.isPositionOnLine(intersect)) return intersect
        }
        return null
    }

    private fun calculateIntersection(vertical: Line, horizontal: Line): Position {
        return Position(vertical.start.x, horizontal.start.y)
    }

    private fun wireToLines(wire: List<String>): List<Line> {
        var current = Position(0, 0)
        val lines = mutableListOf<Line>()

        wire.forEach { instruction ->
            val line = instructionToLine(instruction, current)

            lines.add(line)
            current = line.end
        }

        return lines.toList()
    }

    private fun instructionToLine(instruction: String, position: Position): Line {
        val matcher = INSTRUCTION_REGEX.matcher(instruction)
        matcher.find()

        val operation = matcher.group(OPERATION_GROUP)
        val operand = matcher.group(OPERAND_GROUP).toInt()

        return when (operation) {
            "U" -> Line(position, 0, operand)
            "D" -> Line(position, 0, -operand)
            "R" -> Line(position, operand, 0)
            "L" -> Line(position, -operand, 0)
            else -> throw UnsupportedOperationException("Instruction type $operation not supported")
        }
    }
}