package day13

import common.Position
import common.Utils
import common.intcode.Program
import day13.Main.Tile.TileType.*
import kotlin.math.abs

object Main {

    private const val INPUT = "/day13/input.txt"

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = Utils.getFileFromResource(INPUT).readLines()

        val instructions = Utils.getInputList(lines)
                .map(String::toLong)

        val tiles = generateInitialTiles(instructions)

        val blocks = tiles.filter { it.type == BLOCK }.size

        println("There are $blocks blocks at the start of the game")

        renderGame(tiles)

        val score = playGame(instructions, tiles)

        println("The final score was $score")
    }

    private fun renderGame(tiles: List<Tile>) {
        val maxX = tiles.maxBy { it.position.x }!!.position.x
        val maxY = tiles.maxBy { it.position.y }!!.position.y

        for (y in 0..maxY) {
            for (x in 0..maxX) {
                val tile = tiles.findLast { it.position == Position(x, y) }

                if (tile != null) {
                    print(tile.type.printable)
                } else {
                    print(EMPTY.printable)
                }
            }
            print("\n")
        }
    }

    private fun playGame(instructions: List<Long>, tiles: List<Tile>): Int {
        val instr = instructions.toMutableList()
        val game = mutableListOf<Tile>()
        instr[0] = 2

        val program = Program(instr)

        var score = 0

        while (!program.isExited) {
            // This was not a good day for me
            val ball = program.memory[388]
            val paddle = program.memory[392]

            if (program.inputs.size > 0) {
                program.inputs.removeAll { true }
            }

            program.inputs.add(ball.compareTo(paddle).toLong())

            val x = program.execute()
            val y = program.execute()
            val t = program.execute()

            if (x == -1L && y == 0L) {
                score = t.toInt()
                continue
            }

            game.add(Tile(
                    Position(x.toInt(), y.toInt()),
                    Tile.TileType.fromInt(t.toInt())
            ))
        }

        return score
    }

    private fun calculateBallIntercept(previous: Position, current: Position, yIntercept: Int): Int {
        val dX = current.x - previous.x
        val dY = abs(current.y - previous.y)

        if (dY == 0) {
            return current.x
        }

        val t = (yIntercept - current.y) / dY

        return current.x + (dX * t)
    }

    private fun generateInitialTiles(instructions: List<Long>): MutableList<Tile> {
        val tiles = mutableListOf<Tile>()
        val program = Program(instructions, 0)

        while (!program.isExited) {
            val x = program.execute()
            val y = program.execute()
            val t = program.execute()

            tiles.add(Tile(
                    Position(x.toInt(), y.toInt()),
                    Tile.TileType.fromInt(t.toInt())
            ))
        }
        return tiles
    }

    class Tile(val position: Position, val type: TileType) {
        enum class TileType(val printable: String) {
            EMPTY(" "),
            WALL("█"),
            BLOCK("X"),
            PADDLE("─"),
            BALL("•");

            companion object {
                fun fromInt(int: Int): TileType {
                    return values()[int]
                }
            }
        }
    }
}