package day11

import common.Position
import common.Utils
import common.intcode.Program
import day11.Main.Colour.BLACK
import day11.Main.Colour.WHITE
import day11.Main.Orientation.*

object Main {

    private const val INPUT = "/day11/input.txt"
    @JvmStatic
    fun main(args: Array<String>) {
        val lines = Utils.getFileFromResource(INPUT).readLines()

        val instructions = Utils.getInputList(lines)
                .map(String::toLong)

        var program = Program(instructions)
        var panels = runRobot(mutableListOf(), program)

        println("The robot painted ${panels.map { it.first }.toSet().size} unique panels")

        program = Program(instructions)
        panels = runRobot(mutableListOf(Pair(Position(0, 0), WHITE)), program)

        for (y in (panels.minBy { it.first.y })!!.first.y..panels.maxBy { it.first.y }!!.first.y) {
            for (x in (panels.minBy { it.first.x })!!.first.x..panels.maxBy { it.first.x }!!.first.x) {
                val found = panels.findLast { it.first == Position(x, y) }
                if (found != null && found.second === WHITE) {
                    print("#")
                } else {
                    print(" ")
                }
            }
            println()
        }
    }

    private fun runRobot(panels: MutableList<Pair<Position, Colour>>, program: Program): List<Pair<Position, Colour>> {
        var pos = Position(0, 0)
        var orientation = NORTH

        while (true) {
            val panel = panels.findLast { it.first == pos }
            val colour = panel?.second ?: BLACK

            val paint = program.execute(colour.ordinal.toLong())
            val turn = program.execute()

            if (program.isExited) break

            panels.add(Pair(pos, Colour.values()[paint.toInt()]))

            orientation = orientation.turn(turn.toInt())

            pos = when (orientation) {
                NORTH -> Position(pos.x, pos.y + 1)
                EAST -> Position(pos.x + 1, pos.y)
                SOUTH -> Position(pos.x, pos.y - 1)
                WEST -> Position(pos.x - 1, pos.y)
            }
        }

        return panels
    }

    enum class Colour {
        BLACK, WHITE;
    }

    enum class Orientation {
        NORTH, EAST, SOUTH, WEST;

        fun turn(turn: Int): Orientation {
            return if (turn == 0) {
                values()[normaliseIndex(values().indexOf(this) - 1)]
            } else {
                values()[normaliseIndex(values().indexOf(this) + 1)]
            }
        }

        private fun normaliseIndex(i: Int): Int {
            return when {
                i < 0 -> {
                    values().size - 1
                }
                i >= values().size -> {
                    0
                }
                else -> {
                    i
                }
            }
        }
    }
}