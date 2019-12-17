package day17

import common.Position
import common.Utils
import common.intcode.Program
import day11.Main
import day11.Main.Orientation
import day15.Tile
import day15.Tile.*

object Main {

    private const val INPUT = "/day17/input.txt"

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = Utils.getFileFromResource(INPUT)
                .readLines()

        val instructions = Utils.getInputList(lines)
                .map(String::toLong)

        val grid = mapArea(instructions)

        val intersections = findIntersections(grid)

        println("The sum of the alignment parameters is ${sumAlignmentParameters(intersections)}")

        generateRoute(grid)
    }

    private fun mapArea(instructions : List<Long>): List<Pair<Position, Tile>> {
        val program = Program(instructions)
        val grid = mutableListOf<Pair<Position, Tile>>()

        var row = 0
        var column = 0

        while (!program.isExited) {
            val output = program.execute() ?: break
            val result = Tile.fromChar(output.toChar())

            if (result != RETURN) {
                grid.add(Pair(Position(column, row), result))
                column++
            } else {
                column = 0
                row++
            }
        }

        return grid
    }

    private fun generateRoute(grid : List<Pair<Position, Tile>>) {
        val traversed = mutableListOf<Position>()

        val scaffold = grid
                .filter { it.second == SCAFFOLD }
                .map { it.first }

        var position = grid
                .find { it.second == ROBOT_UP }!!.first

        while (traversed.toSet().size != scaffold.size) {
            position = route(scaffold, position)
            traversed.add(position)
        }

        println(traversed)
    }

    private fun route(scaffold : List<Position>, position: Position): Position {
        val movements = listOf(
                Pair(position.right(), position.orientation.turnRight()),
                Pair(position.above(), position.orientation),
                Pair(position.left(), position.orientation.turnLeft()),
                Pair(position.below(), position.orientation.turn180())
        ).map { (p, o) -> p.orientation = o; p }

        return movements.first { !scaffold.contains(it) }
    }

    private fun findIntersections(grid: List<Pair<Position, Tile>>): List<Position> {
        return grid
                .filter { it.second == SCAFFOLD }
                .filter { gridItem -> gridItem.first.surroundings()
                        .all { isPositionScaffold(grid, it) }
                }.map { it.first }
    }

    private fun isPositionScaffold(grid : List<Pair<Position, Tile>>, position: Position): Boolean {
        return grid.find { it.first == position }?.second === SCAFFOLD
    }

    private fun sumAlignmentParameters(intersections : List<Position>): Int {
        return intersections.map { it.x * it.y }.sum()
    }
}